package utilities;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.blob.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class BlobConnection {
    private static Logger logger = LoggerFactory.getLogger(BlobConnection.class);

    static CloudBlobClient blobClient = null;
    static CloudBlobContainer container = null;

    public static void uploadCsvFileInBlobStorage(String storageConnectionString, String containerName, String uploadFilePath) {
        File sourceFile = new File(uploadFilePath);

        CloudStorageAccount storageAccount = null;
        CloudBlobClient blobClient = null;
        CloudBlobContainer container = null;

        try {
            storageAccount = CloudStorageAccount.parse(storageConnectionString);
            blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(containerName);

            // Create the container if it does not exist with public access.
            logger.info("Creating container: " + container.getName());
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());

            //Listing contents of container
            for (ListBlobItem blobItem : container.listBlobs()) {
                logger.info("URI of blob is: " + blobItem.getUri());
            }

            //Getting a blob reference
            CloudBlockBlob blob = container.getBlockBlobReference(sourceFile.getName());

            //Creating blob and uploading file to it
            logger.info("Uploading the sample file ");
            blob.uploadFromFile(sourceFile.getAbsolutePath());

        } catch (Exception exception) {
            logger.info(String.valueOf(exception));
        }
    }

}
