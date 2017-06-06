package com.allego.scorm;

import com.allego.scorm.lom.ScormPackage;
import com.allego.scorm.util.UnZip;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Arrays;
import java.util.List;


/**
 * Created by e_walker on 19.05.17.
 */
public class S3ScormUploader  implements IScormParser{

    private  static final int SCORM_1_2 = 1;
    private  static final int SCORM_2004 = 2;

    private final String[] SCORM2004SchemaLocations = {"http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd",
                                            "http://www.adlnet.org/xsd/adlcp_v1p3 adlcp_v1p3.xsd",
                                            "http://www.adlnet.org/xsd/adlseq_v1p3 adlseq_v1p3.xsd",
                                            "http://www.adlnet.org/xsd/adlnav_v1p3 adlnav_v1p3.xsd",
                                            "http://www.imsglobal.org/xsd/imsss imsss_v1p0.xsd"};

    private final String[] SCORM12SchemaLocations = {"http://www.imsproject.org/xsd/imscp_rootv1p1p2 imscp_rootv1p1p2.xsd",
                                            "http://www.imsglobal.org/xsd/imsmd_rootv1p2p1 imsmd_rootv1p2p1.xsd",
                                            "http://www.adlnet.org/xsd/adlcp_rootv1p2 adlcp_rootv1p2.xsd"};

    private final String SCORM12Signature ="1.2";

    private final String SCORM2004Signature ="2004";

    private int detectSCORMVersion(Document manifestDocument) throws BadSCORMPackageException {

        NodeList list = manifestDocument.getElementsByTagName("schemaversion");
        if (list.getLength()>0) {
            Node version = list.item(0);
            String signature  = version.getTextContent();
            if (signature.length()>0) {
                if (signature.indexOf(SCORM12Signature)!= -1) {
                    return SCORM_1_2;
                } else if (signature.indexOf(SCORM2004Signature)!= -1) {
                    return SCORM_2004;
                }

            }
        }
        String locationsPlain  = manifestDocument.getDocumentElement().getAttribute("schemaLocation");
        locationsPlain = locationsPlain.trim().replaceAll("\\s{2,}", " ");

        String[] locations =locationsPlain.split("\\s");
        List<String> listSCORM2004 = Arrays.asList(SCORM2004SchemaLocations);

        for (int i=0;i<locations.length;i++) {
            if (listSCORM2004.contains(locations[i])) {
                return SCORM_2004;
            }
        }

        List<String> listSCORM12 = Arrays.asList(SCORM12SchemaLocations);

        for (int i=0;i<locations.length;i++) {
            if (listSCORM12.contains(locations[i])) {
                return SCORM_1_2;
            }
        }


        throw new BadSCORMPackageException("Cannot resolve SCORM version");
    }

    private ScormPackage parseManifest(String manifest, String packageURL) throws ParserConfigurationException, BadSCORMPackageException {

        ScormPackage result = null;
        String version = null;
        ByteArrayInputStream manifestStream = null;
        try {
            manifestStream = new ByteArrayInputStream(manifest.getBytes("UTF-8"));
            Document manifestDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(manifestStream);

            String templateName = null;
             int packageType = detectSCORMVersion(manifestDocument);



            if (packageType == SCORM_1_2) {
                    templateName = "scorm12.xsl";
                    version = IScormParserResult.SCORM_1_2;

            } else {
                    templateName = "scorm2004.xsl";
                version = IScormParserResult.SCORM_2004;
            }


            FileInputStream fileStream = new FileInputStream(S3ScormUploader.class.getResource(templateName).getFile());

            TransformerFactory factory = TransformerFactory.newInstance();
            Templates template = factory.newTemplates(new StreamSource(fileStream));
            Transformer xformer = template.newTransformer();

            Source source = new StreamSource(new ByteArrayInputStream(manifest.getBytes()));
            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

            Result resultDef =  new StreamResult(resultStream);

            xformer.transform(source, resultDef);
            String defSCORMData = resultStream.toString();

            JSONObject json = new JSONObject(defSCORMData);

            result = new ScormPackage(version, json, packageURL);

        } catch (Exception e) {
            throw new ParserConfigurationException(e.getMessage());
        }




        return result;
    }
    private void uploadToS3(String pathToInputScormPackage, String s3AccessKey, String s3SecretKey, String bucket, String parentTargetPath) throws DirectoryAlreadyExistsException, ScormUploadException {
        AWSCredentials credentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        AmazonS3 s3client = new AmazonS3Client(credentials);
        try{
            S3Object folder =  s3client.getObject(bucket,parentTargetPath);

            throw new DirectoryAlreadyExistsException("S3 directory already exists");
        }catch (AmazonServiceException e) {
            String errorCode = e.getErrorCode();
            if (!errorCode.equals("NoSuchKey")) {
                throw e;
            }

        }
        try {


            File file = new File(pathToInputScormPackage);


            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("scorm","1");

            metadata.setContentType("application/zip");
            metadata.setContentLength(file.length());


            FileInputStream fileData =  new FileInputStream(file);
            String filePath;
            if (parentTargetPath.endsWith("/")) {
                filePath = parentTargetPath+"scorm.zip";
            } else {
                filePath = parentTargetPath+"/scorm.zip";
            }



            s3client.putObject(new PutObjectRequest(
                    bucket, filePath,fileData,metadata));

        } catch (AmazonServiceException ase) {
           throw new ScormUploadException(ase.getErrorMessage());
        } catch (AmazonClientException ace) {
            throw new ScormUploadException(ace.getMessage());
        } catch (FileNotFoundException fe) {
            throw new ScormUploadException(fe.getMessage());
        }

    }

    public IScormParserResult parse(String pathToInputScormPackage, String s3AccessKey, String s3SecretKey, String bucket, String parentTargetPath, String bucketURL) {

        UnZip zip = new UnZip(pathToInputScormPackage);
        String packageURL = parentTargetPath + "/" + bucketURL;
        int parserResult = IScormParserResult.STATUS_SUCCESS;
        byte[] data = null;
        try {
            data = zip.getFileContent("imsmanifest.xml");
        } catch (IOException e) {
            parserResult = IScormParserResult.STATUS_INVALID_SCORM_FORMAT;
        }
        if (data == null) {

            return new S3CloudResult(IScormParserResult.STATUS_INVALID_SCORM_FORMAT, null);

        }
        String manifest = new String(data);

        ScormPackage packageSCORM = null;
        try {
            packageSCORM = parseManifest(manifest, packageURL);
        } catch (ParserConfigurationException e) {
            parserResult = IScormParserResult.STATUS_INVALID_SCORM_FORMAT;
        } catch (BadSCORMPackageException e) {
            parserResult = IScormParserResult.STATUS_UNSUPPORTED_SCORM_VERSION;
        }

        try {
            uploadToS3(pathToInputScormPackage, s3AccessKey, s3SecretKey, bucket, parentTargetPath);
        } catch (DirectoryAlreadyExistsException e) {
            parserResult = IScormParserResult.STATUS_AWS_S3_EXCEPTION;
            packageSCORM =  null;
        } catch (ScormUploadException e) {
            parserResult = IScormParserResult.STATUS_AWS_S3_EXCEPTION;
            packageSCORM =  null;
        }

        S3CloudResult result;
        if (packageSCORM != null) {
            result = new S3CloudResult(IScormParserResult.STATUS_SUCCESS, packageSCORM);
        } else {
            result = new S3CloudResult(parserResult, null);
        }


           return result;

    }
}
