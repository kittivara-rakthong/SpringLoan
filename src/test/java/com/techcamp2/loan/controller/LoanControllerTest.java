package com.techcamp2.loan.controller;

import com.techcamp2.loan.constants.LoanError;
import com.techcamp2.loan.exception.LoanException;
import com.techcamp2.loan.model.LoanInfoModel;
import com.techcamp2.loan.service.LoanService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoanControllerTest {

    @Mock
    LoanService loanService;

    @InjectMocks
    LoanController loanController;

    private MockMvc mvc;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        loanController = new LoanController(loanService);
        mvc = MockMvcBuilders.standaloneSetup(loanController)
                .build();

    }

    @DisplayName("Test get loan info by id equals 1 should return loan infomation")
    @Test
    void testGetLoanInfoByIdEquals1() throws Exception
    {
        Long reqParams = 1L;
        LoanInfoModel loanInfoModel = new LoanInfoModel();
        loanInfoModel.setId(1L);
        loanInfoModel.setStatus("Ok");
        loanInfoModel.setAccountPayable("102-222-2200");
        loanInfoModel.setAccountReceivable("102-222-2200");
        loanInfoModel.setPrincipalAmount(3000000.00);

        when(loanService.getLoanInfoById(reqParams)).thenReturn(loanInfoModel);

        MvcResult mvcResult = mvc.perform(get("/loan/info/"+reqParams))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONObject resp = new JSONObject(mvcResult.getResponse().getContentAsString());
        JSONObject status = new JSONObject(resp.getString("status"));
        JSONObject data = new JSONObject(resp.getString("data"));

        assertEquals("0",status.get("code").toString());
        assertEquals("success",status.get("message").toString());
        assertEquals(1,data.get("id"));
        assertEquals("Ok",data.get("status"));
        assertEquals("102-222-2200",data.get("account_payable"));
        assertEquals("102-222-2200",data.get("account_receivable"));
        assertEquals(3000000,data.get("principal_amount"));

        verify(loanService,times(1)).getLoanInfoById(reqParams);

    }

    @DisplayName("Test get loan info by id equals 2 should throw Loan Exception loan info not found")
    @Test
    void testGetLoanInfoByIdEquals2() throws Exception
    {
        Long reqParams = 2L;
        when(loanService.getLoanInfoById(reqParams)).thenThrow(
                new LoanException(LoanError.GET_LOAN_INFO_NOT_FOUND, HttpStatus.BAD_REQUEST)
        );

        MvcResult mvcResult = mvc.perform(get("/loan/info/"+reqParams))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONObject resp = new JSONObject(mvcResult.getResponse().getContentAsString());
        JSONObject status = new JSONObject(resp.getString("status"));

        assertEquals("LOAN4002",status.get("code").toString());
        assertEquals("Loan infomation not found",status.get("message").toString());

    }

    @DisplayName("Test get loan info by id equals 3 should throw Loan Exception loan info not found")
    @Test
    void testGetLoanInfoByIdEquals3() throws Exception
    {
        Long reqParams = 3L;
        when(loanService.getLoanInfoById(reqParams)).thenThrow(
                new Exception("Test throws new exception")
        );

        MvcResult mvcResult = mvc.perform(get("/loan/info/"+reqParams))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONObject resp = new JSONObject(mvcResult.getResponse().getContentAsString());
        JSONObject status = new JSONObject(resp.getString("status"));

        assertEquals("LOAN4001",status.get("code").toString());
        assertEquals("Cannot get loan infomation",status.get("message").toString());

    }

}
