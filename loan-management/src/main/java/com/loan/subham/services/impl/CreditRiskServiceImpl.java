package com.loan.subham.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.loan.subham.repositories.CreditRiskRepo;
import com.loan.subham.repositories.LoanAppRepo;
import com.loan.subham.entities.CreditRisk;
import com.loan.subham.entities.LoanApp;
import com.loan.subham.payloads.CreditRiskDTO;
import com.loan.subham.payloads.LoanAppDTO;
import com.loan.subham.services.CreditRiskService;

@Service
public class CreditRiskServiceImpl implements CreditRiskService {
	
	@Autowired
	private CreditRiskRepo CreditRiskRepo;
	@Autowired
	private LoanAppRepo loanAppRepo;
	@Autowired
	private LoanAppServiceImpl loanAppServiceImpl;

	@Override
	public CreditRiskDTO createCreditRisk(CreditRiskDTO creditRiskDto, String loanAppId) {
		// TODO Auto-generated method stub
		CreditRisk CreditRisk = this.creditRiskDTOToCreditRisk(creditRiskDto);
		LoanApp loanApp = this.loanAppRepo.findById(loanAppId).get();
		CreditRisk.setLoanApplication(loanApp);
		this.CreditRiskRepo.save(CreditRisk);
		
		creditRiskDto.setLoanApplicationDto(this.loanAppServiceImpl.LoanApplicationToloanAppDto(loanApp));
		return creditRiskDto;
	}
	
	
	@Override
	public String checkCreditScore(LoanAppDTO loanAppDto) {
		// TODO Auto-generated method stub
		String result;
		String id = "CR" + loanAppDto.getLoanAppId();
		CreditRisk creditRisk = this.CreditRiskRepo.findById(id).get();
		if(creditRisk.getCreditScore()>500) {
			result = "Eligible";
		}
		else {
			result = "Non-Eligible";
		}
		loanAppDto.setStatus(result);
		this.loanAppServiceImpl.updateLoanApp(loanAppDto);
		return result;
		
	}
	
	
	
	//converter functions ....
	public CreditRiskDTO creditRiskToCreditRiskDTO(CreditRisk CreditRisk) {
		CreditRiskDTO creditRiskDto = new CreditRiskDTO();
		creditRiskDto.setBasicCheck(CreditRisk.getBasicCheck());
		creditRiskDto.setCreditScore(CreditRisk.getCreditScore());
		creditRiskDto.setEmi(CreditRisk.getEmi());
		creditRiskDto.setCrId(CreditRisk.getCrId());
		//creditRiskDto.setLoanApplicationDto(this.loanAppServiceImpl.LoanApplicationToloanAppDto(creditRisk.getLoanApplication()));
		return creditRiskDto;
	}
	
	public CreditRisk creditRiskDTOToCreditRisk(CreditRiskDTO creditRiskDto) {
		CreditRisk CreditRisk = new CreditRisk();
		CreditRisk.setBasicCheck(creditRiskDto.getBasicCheck());
		CreditRisk.setCreditScore(creditRiskDto.getCreditScore());
		CreditRisk.setEmi(creditRiskDto.getEmi());
		CreditRisk.setCrId(creditRiskDto.getCrId());		
		//creditRisk.setLoanApplication(this.loanAppServiceImpl.loanAppDtoToLoanApplication(creditRiskDto.getLoanApplicationDto()));	
		return CreditRisk;
	}

	
	//for finding credit risk field by loanAppId...
	@Override
	public CreditRisk findByLoanAppId(String loanAppId) {
		// TODO Auto-generated method stub
		String id = "CR" + loanAppId;
		CreditRisk creditRisk = this.CreditRiskRepo.findById(id).get();
		return creditRisk;
	}


	@Override
	public CreditRiskDTO viewById(String crId) {
		// TODO Auto-generated method stub
		CreditRisk creditRisk = this.CreditRiskRepo.findById(crId).get();
		return this.creditRiskToCreditRiskDTO(creditRisk);
	}


	

}
