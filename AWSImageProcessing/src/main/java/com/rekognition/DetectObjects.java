package com.rekognition;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.util.IOUtils;

//https://docs.aws.amazon.com/rekognition/latest/dg/labels-detect-labels-image.html
public class DetectObjects {
	
    public static void main(String[] args) throws Exception {
		String photo = "mobile.jpeg";

		ClassLoader classLoader = new DetectObjects().getClass().getClassLoader();
		File file = new File(classLoader.getResource(photo).getFile());

		ByteBuffer imageBytes;
		try (InputStream inputStream = new FileInputStream(file)) {
			imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		}

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("ap-south-1").build();

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withBytes(imageBytes))
                .withMaxLabels(10)
                .withMinConfidence(77F);

        try {

            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List <Label> labels = result.getLabels();

            System.out.println("Detected labels for " + photo);
            for (Label label: labels) {
               System.out.println(label.getName() + ": " + label.getConfidence().toString());
            }

        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        }

    }
}