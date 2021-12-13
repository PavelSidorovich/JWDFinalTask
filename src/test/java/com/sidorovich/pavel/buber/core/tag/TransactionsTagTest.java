package com.sidorovich.pavel.buber.core.tag;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.math.BigDecimal;
import java.util.Arrays;

public class TransactionsTagTest {

    @Mock
    private PageContext pageContext;

    @InjectMocks
    private TransactionsTag transactionsTag;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionsTag.setTitle("title");
        transactionsTag.setCurrency("RUB");
        transactionsTag.setSymbol('+');
        transactionsTag.setTransactions(Arrays.asList(BigDecimal.ZERO, BigDecimal.ONE));
    }

    @Test
    public void doStartTag_shouldReturnZeroValue_whenEverythingIsValid() {
        JspWriter jspWriter = Mockito.mock(JspWriter.class);
        Mockito.when(pageContext.getOut()).thenReturn(jspWriter);

        try {
            Assert.assertSame(transactionsTag.doStartTag(), 0);
        } catch (JspException ignored) {
        }
    }

    @Test
    public void doEndTag_shouldReturnSix_whenEverythingIsValid() {
        try {
            Assert.assertSame(transactionsTag.doEndTag(), 6);
        } catch (JspException ignored) {
        }
    }

}