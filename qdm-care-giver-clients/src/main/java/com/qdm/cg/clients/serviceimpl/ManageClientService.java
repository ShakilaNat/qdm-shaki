package com.qdm.cg.clients.serviceimpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qdm.cg.clients.dto.ClientActivityDto;
import com.qdm.cg.clients.dto.ClientActivityResponse;
import com.qdm.cg.clients.dto.ClientActivitySummaryDto;
import com.qdm.cg.clients.dto.ClientInfoDto;
import com.qdm.cg.clients.dto.ClientReportResponse;
import com.qdm.cg.clients.dto.EquipmentDto;
import com.qdm.cg.clients.dto.IssueDetailDto;
import com.qdm.cg.clients.dto.IssueDto;
import com.qdm.cg.clients.dto.IssueListResponse;
import com.qdm.cg.clients.dto.IssueStatus;
import com.qdm.cg.clients.dto.ProductRatingDto;
import com.qdm.cg.clients.dto.ProductRatingResponse;
import com.qdm.cg.clients.dto.ProductsDto;
import com.qdm.cg.clients.dto.RecommendationsDto;
import com.qdm.cg.clients.dto.RecommendationsTrackResponse;
import com.qdm.cg.clients.dto.RecommendedProductsDto;
import com.qdm.cg.clients.dto.RecommendedProductsResponse;
import com.qdm.cg.clients.dto.ReportsDto;
import com.qdm.cg.clients.dto.TimeLine;
import com.qdm.cg.clients.entity.ClientDetails;
import com.qdm.cg.clients.entity.Equipment;
import com.qdm.cg.clients.entity.Issues;
import com.qdm.cg.clients.entity.Product;
import com.qdm.cg.clients.entity.Reports;
import com.qdm.cg.clients.enums.ManageClientsConstants;
import com.qdm.cg.clients.enums.StatusEnum;
import com.qdm.cg.clients.exceptionhandler.NoIssueFoundException;
import com.qdm.cg.clients.repository.EquipmentRepository;
import com.qdm.cg.clients.repository.IssuesRepository;
import com.qdm.cg.clients.repository.ProductRepository;
import com.qdm.cg.clients.repository.ReportsRepository;
import com.qdm.cg.clients.response.ResponseInfo;

@Service
public class ManageClientService {

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	IssuesRepository issuesRepository;
	@Autowired
	ReportsRepository reportsRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	EquipmentRepository equipmentRepository;

	public ResponseInfo getClientReport(long clientId) {

		List<ReportsDto> reportsList = new ArrayList<ReportsDto>();
		List<Reports> entityList = reportsRepository.findByClientId(clientId);
		for (Reports entity : entityList) {
			reportsList.add(modelMapper.map(entity, ReportsDto.class));
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(ClientReportResponse.builder().reports(reportsList).total_reports(reportsList.size()).build())
				.build();
	}

	public ResponseInfo getIssueDetail(long issueId) {

		Issues isssue=issuesRepository.findById(issueId).orElseThrow(()-> new NoIssueFoundException(issueId+ " Issue ID not found."));
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(modelMapper.map(isssue, IssueDetailDto.class))
				.build();
	}

	public ResponseInfo getIssueList(long clientId) {
		int openCount = 0, resolvedCount = 0, pendingCount = 0;
		List<Issues> issueList = issuesRepository.findByClientId(clientId);
		List<IssueDto> issuedto = new ArrayList<>();
		for (Issues entity : issueList) {
			issuedto.add(modelMapper.map(entity, IssueDto.class));
		}
		for (IssueDto dto : issuedto) {

			if (dto.getIssue_status().equalsIgnoreCase(ManageClientsConstants.open_status)) {
				openCount++;
			} else if (dto.getIssue_status().equalsIgnoreCase(ManageClientsConstants.resolved_status)) {
				resolvedCount++;
			} else if (dto.getIssue_status().equalsIgnoreCase(ManageClientsConstants.resolved_status)) {
				pendingCount++;
			}
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(IssueListResponse.builder().open_count(openCount).pending_count(pendingCount)
						.resolved_count(resolvedCount).issues_enum(StatusEnum.values()).issue_list(issuedto).build())
				.build();

	}
	
	public ResponseInfo modifyIssueStatus(IssueStatus issueStatus) {

		Issues issues = issuesRepository.findById(issueStatus.getIssue_id())
				.orElseThrow(() -> new NoIssueFoundException(issueStatus.getIssue_id() + "  Issue ID was Not found."));

		issues.setIssue_status(issueStatus.getIssue_status());
		issuesRepository.save(issues);
		return ResponseInfo.builder().status("Success").status_code(200).message("").build();

	}

	public ResponseInfo getRecommendations() {
		List<Equipment> equipments = equipmentRepository.findAll();
		List<EquipmentDto> equipmentList = new ArrayList<EquipmentDto>();
		for(Equipment equipment:equipments) {
			equipmentList.add(modelMapper.map(equipment, EquipmentDto.class));
		}
		List<Product> products = productRepository.findAll();
		List<ProductsDto> productList = new ArrayList<ProductsDto>();
		for(Product product:products) {
			productList.add(modelMapper.map(product, ProductsDto.class));
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(RecommendationsDto.builder().equipments(equipmentList).products(productList).build()).build();

	}
	
	public ResponseInfo getProductRatings(long clientId) {
		List<Product> productsList=productRepository.findByClientId(clientId);
		List<ProductRatingDto> productRatings = new ArrayList<ProductRatingDto>();
		for(Product product:productsList) {
			productRatings.add(modelMapper.map(product, ProductRatingDto.class));
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(ProductRatingResponse.builder().ratings_list(productRatings).build()).build();

	}
	//done

	public ResponseInfo getActivitySummary() {
		ClientInfoDto clientInfo = new ClientInfoDto("ZeroSpacer", "male", 21, "9876543210", "87.09", "78.90");
		ClientActivitySummaryDto clientSummary = new ClientActivitySummaryDto(1, "test", "test", "08-24-2020", "",
				"This assessment is to help", clientInfo);
		return ResponseInfo.builder().status("Success").status_code(200).message("").data(clientSummary).build();

	}

	public ResponseInfo getClientActivity(String event) {

		ClientActivityDto clientInfo = new ClientActivityDto(1, "Pre-Assessment tips to Mia", "Mia Queen",
				"27-09-2020 17:02:09", false);
		ClientActivityDto clientInfo1 = new ClientActivityDto(2, "Pre-Assessment tips to Mia2", "Mia Queen",
				"27-10-2020 15:02:09", false);
		ClientActivityDto clientInfo2 = new ClientActivityDto(3, "Pre-Assessment tips to Mia3", "Mia Queen",
				"26-08-2020 17:02:09", true);
		ClientActivityDto clientInfo3 = new ClientActivityDto(4, "Pre-Assessment tips to Mia4", "Mia Queen",
				"25-08-2020 17:02:09", false);
		List<ClientActivityDto> activityList = new ArrayList<ClientActivityDto>();
		activityList.add(clientInfo);
		activityList.add(clientInfo1);
		activityList.add(clientInfo2);
		activityList.add(clientInfo3);
		if (event == null) {
			return ResponseInfo.builder().status("Success").status_code(200).message("")
					.data(ClientActivityResponse.builder().activities(activityList).build()).build();
		}
		List<ClientActivityDto> pastActivityList = new ArrayList<ClientActivityDto>();
		List<ClientActivityDto> upcomingActivityList = new ArrayList<ClientActivityDto>();
		String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
		SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");

		for (ClientActivityDto activity : activityList) {
			try {
				Date d1 = sdfo.parse(date);
				Date d2 = sdfo.parse(activity.getDate_time());
				if (d1.compareTo(d2) > 0) {
					pastActivityList.add(activity);
				} else {
					upcomingActivityList.add(activity);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if ("past".equals(event)) {
			return ResponseInfo.builder().status("Success").status_code(200).message("")
					.data(ClientActivityResponse.builder().activities(pastActivityList).build()).build();

		} else if ("upcoming".equals(event)) {
			return ResponseInfo.builder().status("Success").status_code(200).message("")
					.data(ClientActivityResponse.builder().activities(upcomingActivityList).build()).build();

		}
		return null;

	}

	public ResponseInfo getRecommendedProductList() {
		RecommendedProductsDto recommendedProduct = new RecommendedProductsDto("Ramsons Angel Nebulizer EBS 028933", 1,
				"MYR 432", "Purchased", "26-08-2020 15:02:09", "https://picsum.photos/200/300?random=1");
		// RecommendedProductsResponse
		List<RecommendedProductsDto> recommendedProductList = new ArrayList<RecommendedProductsDto>();
		recommendedProductList.add(recommendedProduct);
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(RecommendedProductsResponse.builder().recommended_products_list(recommendedProductList).build())
				.build();
	}

	public ResponseInfo getRecommendedProductTrack() {
		RecommendationsTrackResponse response = new RecommendationsTrackResponse();
		List<TimeLine> timeLine = new ArrayList<TimeLine>();
		timeLine.add(new TimeLine("Recommended", "24-08-2020", true));
		timeLine.add(new TimeLine("Consent Received", "24-08-2020", true));
		timeLine.add(new TimeLine("Purchased", "24-08-2020", true));
		timeLine.add(new TimeLine("Delivered", "24-08-2020", false));
		timeLine.add(new TimeLine("Demoed", "24-08-2020", false));
		response.setProduct_id(1);
		response.setProduct_name("Zerostat spacer");
		response.setProduct_price("MYR 432");
		response.setCurrent_status("Recommended on July 04");
		response.setTimeline(timeLine);
		return ResponseInfo.builder().status("Success").status_code(200).message("").data(response).build();

	}

	

}