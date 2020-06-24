package com.techcamp2.loan.controller;



import com.techcamp2.loan.constants.LoanError;
import com.techcamp2.loan.constants.Response;
import com.techcamp2.loan.exception.LoanException;
import com.techcamp2.loan.model.LoanInfoModel;
import com.techcamp2.loan.model.ResponseModel;
import com.techcamp2.loan.model.StatusModel;
import com.techcamp2.loan.service.LoanService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path ="/loan")
public class LoanController {

    private static final Logger log = LogManager.getLogger(LoanController.class.getName());
    private LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService)
    {
        this.loanService = loanService;
    }

    @GetMapping("/info/{id}")
    public HttpEntity<ResponseModel> getLoanInfoById(@PathVariable Long id) throws Exception
    {
        try
        {
            log.info("Get loan info by customer id:{}",id);
            LoanInfoModel loanInfoResponse = loanService.getLoanInfoById(id);
            log.info("Response id: {},status is: {}",loanInfoResponse.getId(),loanInfoResponse.getStatus());
            StatusModel status = new StatusModel(Response.SUCCESS_CODE.getContent(),Response.SUCCESS.getContent());
            return ResponseEntity.ok(new ResponseModel(status,loanInfoResponse));
        }
        catch(LoanException e)
        {
            log.error("Loan Exception by id: {}");
            LoanError loanError = e.getLoanError();
            return ResponseEntity.ok(
                    new ResponseModel(
                            new StatusModel(
                                    loanError.getCode(),
                                    loanError.getMessage())
                    )
            );
        }
        catch (Exception e)
        {
            log.error("Exception by id: {}");
            LoanError loanError = LoanError.GET_LOAN_INFO_EXCEPTION;
            return new ResponseModel(
                    new StatusModel(
                            loanError.getCode(),
                            loanError.getMessage())
                    ).build(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
