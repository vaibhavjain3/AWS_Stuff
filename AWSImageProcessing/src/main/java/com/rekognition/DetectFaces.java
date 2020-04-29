package com.rekognition;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AgeRange;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

//https://docs.aws.amazon.com/rekognition/latest/dg/faces-detect-images.html
public class DetectFaces {
   
   public static void main(String[] args) throws Exception {

		String photo = "people.jpeg";

		ClassLoader classLoader = new DetectObjects().getClass().getClassLoader();
		File file = new File(classLoader.getResource(photo).getFile());

		ByteBuffer imageBytes;
		try (InputStream inputStream = new FileInputStream(file)) {
			imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		}

		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("ap-south-1").build();

      /* If we want to fetch photo from bucket
      DetectFacesRequest request = new DetectFacesRequest()
         .withImage(new Image()
            .withS3Object(new S3Object()
               .withName(photo)
               .withBucket(bucket)))
         .withAttributes(Attribute.ALL);*/
      
      DetectFacesRequest request = new DetectFacesRequest()
    	         .withImage(new Image()
                         .withBytes(imageBytes))
    	         .withAttributes(Attribute.ALL);

      try {
         DetectFacesResult result = rekognitionClient.detectFaces(request);
         List < FaceDetail > faceDetails = result.getFaceDetails();

         System.out.println("Faces detected:" + faceDetails.size());
         System.out.println();
         for (FaceDetail face: faceDetails) {
            if (request.getAttributes().contains("ALL")) {
               AgeRange ageRange = face.getAgeRange();
               System.out.println("The detected face is estimated to be between "
                  + ageRange.getLow().toString() + " and " + ageRange.getHigh().toString()
                  + " years old.");
               System.out.println("Here's the complete set of attributes:");
            } else { // non-default attributes have null values.
               System.out.println("Here's the default set of attributes:");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
         }

      } catch (AmazonRekognitionException e) {
         e.printStackTrace();
      }

   }

}