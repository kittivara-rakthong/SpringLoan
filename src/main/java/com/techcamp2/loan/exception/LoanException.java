package com.techcamp2.loan.exception;

import com.techcamp2.loan.constants.LoanError;
import com.techcamp2.loan.constants.Response;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class LoanException extends Exception{
    private LoanError loanError;
    private HttpStatus httpStatus = HttpStatus.OK;

    public LoanException(LoanError loanError, HttpStatus httpStatus)
    {
        this.loanError = loanError;
        this.httpStatus = httpStatus;
    }
}
