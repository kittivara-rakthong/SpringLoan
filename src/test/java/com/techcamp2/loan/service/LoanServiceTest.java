package com.techcamp2.loan.service;

import com.techcamp2.loan.exception.LoanException;
import com.techcamp2.loan.model.LoanInfoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoanServiceTest {
    @InjectMocks
    LoanService loanService;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        loanService = new LoanService();
    }

    @DisplayName("Test get loan info by id equals 1 should return loan infomation")
    @Test
    void testGetLoanInfoByIdEquals1() throws Exception
    {
        LoanInfoModel resp = loanService.getLoanInfoById(1L);

        assertEquals("1",resp.getId().toString());
        assertEquals("Ok",resp.getStatus());
        assertEquals("102-222-2200",resp.getAccountPayable());
        assertEquals("102-222-2200",resp.getAccountReceivable());
        assertEquals(4000000.00,resp.getPrincipalAmount());

    }
    @DisplayName("Test get loan info by id equals 2 should throw Loan Exception loan info not found")
    @Test
    void testGetLoanInfoByIdEquals2() throws Exception
    {
        Long reqParams = 2L;
        LoanException thrown = assertThrows(LoanException.class,
                () ->loanService.getLoanInfoById(reqParams),
                "Expected loanInfoById(reqParams) to throw,but it didn't");
        assertEquals(400,thrown.getHttpStatus().value());
        assertEquals("LOAN4002",thrown.getLoanError().getCode());
        assertEquals("Loan infomation not found",thrown.getLoanError().getMessage());

    }

    @DisplayName("Test get loan info by id equals 2 should throw Loan Exception loan info not found")
    @Test
    void testGetLoanInfoByIdEquals3() throws Exception
    {
        Long reqParams = 3L;
        Exception thrown = assertThrows(Exception.class,
                () ->loanService.getLoanInfoById(reqParams),
                "Expected loanInfoById(reqParams) to throw,but it didn't");

        assertEquals("Test throw new exception",thrown.getMessage());

    }
}
