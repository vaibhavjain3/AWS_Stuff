package com.rekognition;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;

public class AccoProd {

	public static void main(String[] args) throws Exception {

		String photo = "http://cdn.eduthrill.com/userGameImage/d6dff032-60e6-4bba-ab88-6e415f946866-1570903466173.jpeg";

		ByteBuffer imageBytes;
		try (InputStream inputStream = new URL(photo).openStream()) {
			imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		}
		DetectFacesRequest request = new DetectFacesRequest().withImage(new Image().withBytes(imageBytes))
				.withAttributes(Attribute.ALL);

		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("ap-south-1")
				.build();

		try {
			DetectFacesResult result = rekognitionClient.detectFaces(request);
			List<FaceDetail> faceDetails = result.getFaceDetails();

			if (faceDetails.size() > 1) {
				System.out.println("Plagiarism");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
