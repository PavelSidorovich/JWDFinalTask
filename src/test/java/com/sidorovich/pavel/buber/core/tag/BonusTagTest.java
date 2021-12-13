package com.sidorovich.pavel.buber.core.tag;

import com.sidorovich.pavel.buber.api.model.Bonus;
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
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BonusTagTest {

    @Mock
    private PageContext pageContext;

    @InjectMocks
    private BonusTag bonusTag;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bonusTag.setBonus(
                new Bonus(
                        1L, 20d,
                        Date.valueOf(LocalDate.of(2020, 12, 20)),
                        null
                )
        );
        bonusTag.setLocalizationContext(new LocalizationContext(ResourceBundle.getBundle("test")));
    }

    @Test
    public void doStartTag_shouldReturnZeroValue_whenEverythingIsValid() {
        JspWriter jspWriter = Mockito.mock(JspWriter.class);
        Mockito.when(pageContext.getOut()).thenReturn(jspWriter);

        try {
            Assert.assertSame(bonusTag.doStartTag(), 0);
        } catch (JspException ignored) {
        }
    }

    @Test
    public void doEndTag_shouldReturnSix_whenEverythingIsValid() {
        try {
            Assert.assertSame(bonusTag.doEndTag(), 6);
        } catch (JspException ignored) {
        }
    }

}