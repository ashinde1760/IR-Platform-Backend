package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.owasp.encoder.Encode;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import jakarta.inject.Inject;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

@Controller("/investor/shareholdermeeting")
public class ShareHolderMeetingController {

	@Inject
	private ShareHolderMeetingService service;

	@Inject
	private AuditHistoryService auditHistoryService;

	@Post(uri = "/add", consumes = { MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	public MutableHttpResponse<ShareHolderMeetingEntity> addShareholderDataformDetails(
			@Body ShareHolderMeetingEntity shareholdermeetingEntity, CompletedFileUpload momfile)
			throws SQLException, ShareHolderMeetingControllerExcetion {

		try {

			System.out.println("shareholdermeetingEntity: ===---===>" + shareholdermeetingEntity.getClientName());
			ShareHolderMeetingEntity newshareholdermeeting = this.service
					.createShareHolderMeeting(shareholdermeetingEntity, momfile);
			return HttpResponse.status(HttpStatus.OK).body(newshareholdermeeting);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ShareHolderMeetingControllerExcetion(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}

	}

	@Post(uri = "/extractMOMfile", consumes = { MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	public JSONObject extractMomFile(String meetingId, CompletedFileUpload momfile)
			throws ShareHolderMeetingControllerExcetion {
		try {

			JSONObject response = this.service.extractMomFiledetails(meetingId, momfile);
			return response;
		} catch (Exception e) {
			throw new ShareHolderMeetingControllerExcetion(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Get("getPreviewForMOMfile/{shareholderid}")
	public MutableHttpResponse<StreamedFile> getPreviewForMOMfile(@PathVariable("shareholderid") String shareholderid)
			throws ShareHolderMeetingControllerExcetion {
		try {
			ShareHolderMeetingEntity entity = this.service.getPreviewForMomFile(shareholderid);
			byte[] byteArray = entity.getMomFileData();
			byte[] pdfData = convertDocxToPdf(byteArray);
			InputStream inputStream = new ByteArrayInputStream(pdfData);
			StreamedFile streamedFile = new StreamedFile(inputStream, MediaType.APPLICATION_PDF_TYPE);

			return HttpResponse.ok().contentType(MediaType.APPLICATION_PDF_TYPE)
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + entity.getMomfileName())
					.body(streamedFile);
		} catch (Exception e) {
			throw new ShareHolderMeetingControllerExcetion(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	private byte[] convertDocxToPdf(byte[] byteArray) throws IOException {

		try (PDDocument pdfDoc = new PDDocument()) {
			// Convert DOCX to text
			XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(byteArray));
			XWPFWordExtractor wordExtractor = new XWPFWordExtractor(doc);
			String content = wordExtractor.getText();

			// Create a PDF page and add the content to it
			PDPage page = new PDPage(PDRectangle.A4); // You can adjust the page size if needed
			pdfDoc.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page);
			contentStream.setFont(PDType1Font.HELVETICA, 12); // Use Helvetica font

			// Split the content into lines to handle line breaks
			String[] lines = content.split("\n");
			int y = 700; // Starting y-coordinate
			for (String line : lines) {
				contentStream.beginText();
				contentStream.newLineAtOffset(100, y); // Set the starting position for the text
				contentStream.showText(line); // Write the line to the PDF
				contentStream.endText();
				y -= 15; // Adjust the y-coordinate for the next line
			}

			contentStream.close();

			// Save PDF to byte array
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			pdfDoc.save(byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		}
	}

	@Get("downloadMOMfile/{shareholderid}/{createdBy}")
	public MutableHttpResponse<InputStream> downloadMomfile(@PathVariable("shareholderid") String shareholderid,
			@PathVariable("createdBy") String createdBy) throws ShareHolderMeetingControllerExcetion {
		System.out.println("created by: " + createdBy);
		try {
			ShareHolderMeetingEntity entity = this.service.getPreviewForMomFile(shareholderid);

			Date d = new Date();
			AuditHistoryEntity entiAuditHistoryEntity = new AuditHistoryEntity();
			entiAuditHistoryEntity.setCreatedBy(createdBy);
			entiAuditHistoryEntity.setActivity("download MOM file");
			entiAuditHistoryEntity.setDescription("Download MOM file ");
			entiAuditHistoryEntity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entiAuditHistoryEntity);

			byte[] byteArray = entity.getMomFileData();
			InputStream inputStream = new ByteArrayInputStream(byteArray);

			// Set the filename and content disposition header for the download
			String filename = entity.getMomfileName() + ".docx";
			String disposition = "attachment; filename=\"" + filename + "\"";

			// Return the HTTP response with the DOCX content and headers
			return HttpResponse.ok(inputStream).contentType(MediaType.APPLICATION_OCTET_STREAM_TYPE) // Set content type
																										// to generic
																										// binary
					.header(HttpHeaders.CONTENT_DISPOSITION, disposition);
		} catch (Exception e) {
			throw new ShareHolderMeetingControllerExcetion(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Get("getMeetingdataById/{shareholderid}")
	public ShareHolderMeetingEntity getDataById(@PathVariable("shareholderid") String shareholderid)
			throws ShareHolderMeetingControllerExcetion {
		try {
			ShareHolderMeetingEntity response = this.service.getShareHolderMeetingById(shareholderid);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderMeetingControllerExcetion(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

//	@Get("converttoText/{holderid}")
//	public String convertToText(@PathVariable("holderid") String holderid)
//			throws ShareHolderMeetingControllerExcetion {
//	
//	        try {
//	        	
//	        	 GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("data/cred.json"));
//
//	             SpeechClient speechClient = SpeechClient.create(SpeechSettings.newBuilder()
//	                     .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
//	                     .build());
//
//	             byte[] audioData = this.service.getShareHolderMeetingById(holderid);
//	             InputStream audioInputStream = new ByteArrayInputStream(audioData);
//
//	             // Set recognition config
//	             RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
//	                     .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
//	                     .setSampleRateHertz(16000)
//	                     .setLanguageCode("en-US")
//	                     .setEnableAutomaticPunctuation(true)
//	                     .setAudioChannelCount(1) // Set to 1 for mono audio
//	                     .build();
//
//	             // Set recognition audio
//	             RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
//	                     .setContent(ByteString.copyFrom(audioData))
//	                     .build();
//
//	             // Create recognition request
//	             RecognizeRequest recognizeRequest = RecognizeRequest.newBuilder()
//	                     .setConfig(recognitionConfig)
//	                     .setAudio(recognitionAudio)
//	                     .build();
//
//	             // Perform speech recognition
//	             RecognizeResponse recognizeResponse = speechClient.recognize(recognizeRequest);
//
//	             StringBuilder transcriptBuilder = new StringBuilder();
//	             List<SpeechRecognitionResult> resultsList = recognizeResponse.getResultsList();
//	             for (SpeechRecognitionResult result : resultsList) {
//	                 SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//	                 String transcript = alternative.getTranscript();
//	                 transcriptBuilder.append(transcript).append(" ");
//	             }
//
//	             speechClient.close();
//
//	             return transcriptBuilder.toString().trim();
//	         } catch (Exception e) {
//	             throw new ShareHolderMeetingControllerExcetion(ReadPropertiesFile.readResponseProperty("101"), e, 400,
//	                     e.getMessage());
//	         }
//	     }
//	

	@Get("/list")
	public List<ShareHolderMeetingEntity> getDetails() throws SQLException, ShareHolderMeetingControllerExcetion {
		try {
			List<ShareHolderMeetingEntity> shareholderData = this.service.getShareHolderMeetingDetails();
			 for (ShareHolderMeetingEntity item : shareholderData) {
		            item.setClientName(escapeHtml(item.getClientName())); // Properly escape HTML
		        }
			return shareholderData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ShareHolderMeetingControllerExcetion(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	 private static String escapeHtml(String input) {
	        // Using OWASP Java Encoder
	        return Encode.forHtmlContent(input);
	    }
	@Patch("/{shareholderid}")
	public HttpResponse<ShareHolderMeetingEntity> updateShareholder(
			@Body ShareHolderMeetingEntity shareholdermeetingEntity,
			@PathVariable("shareholderid") String shareholderid) throws ShareHolderMeetingControllerExcetion {
		try {
			ShareHolderMeetingEntity updatedshareholder = this.service
					.updateShareHolderMeeting(shareholdermeetingEntity, shareholderid);
			return HttpResponse.status(HttpStatus.OK).body(updatedshareholder);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ShareHolderMeetingControllerExcetion(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Delete("/{shareholderid}")
	public MutableHttpResponse<String> deleteShareholder(@PathVariable("shareholderid") String shareholderid)
			throws ShareHolderMeetingControllerExcetion {
		try {
			String response = this.service.deleteShareHoderMeeting(shareholderid);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ShareHolderMeetingControllerExcetion(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

}
