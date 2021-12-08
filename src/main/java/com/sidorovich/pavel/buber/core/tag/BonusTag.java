package com.sidorovich.pavel.buber.core.tag;

import com.sidorovich.pavel.buber.api.model.Bonus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class BonusTag extends TagSupport {

    private static final Logger LOG = LogManager.getLogger(BonusTag.class);

    private static final String BONUS_CARD =
            "<div class='card %s shadow'>"
            + "<h4 style='font-size: 100px' class='card-title text-center'>%.0f%%</h4>"
            + "<ul class='list-group list-group-flush'>"
            + "<li class='list-group-item'>Gives a %.2f%% discount when making an order</li>"
            + "<li class='list-group-item'>Expires: %s</li>"
            + "</ul>"
            + "</div>";
    private static final String DEFAULT_COLOR = "bg-dark";
    private static final double SIXTY_PERCENTS = 60d;
    private static final double FOURTY_PERCENTS = 40d;
    private static final double FIFTEEN_PERCENTS = 15d;
    private static final double ZERO_PERCENTS = 0d;
    private static final String GREEN_COLOR = "bg-success";
    private static final String BLUE_COLOR = "bg-primary";
    private static final String YELLOW_COLOR = "bg-warning";
    private static final String RED_COLOR = "bg-danger";

    private Bonus bonus;

    @Override
    public int doStartTag() throws JspException {
        final Double discount = bonus.getDiscount();
        final String cardColor = getCardColor(discount);
        final String cardFormatted = String.format(
                BONUS_CARD, cardColor, discount,
                discount, bonus.getExpireDate()
        );

        printBonus(cardFormatted);

        return SKIP_BODY;
    }

    private String getCardColor(Double discount) {
        if (discount.compareTo(SIXTY_PERCENTS) > 0) {
            return GREEN_COLOR;
        } else if (discount.compareTo(FOURTY_PERCENTS) > 0) {
            return BLUE_COLOR;
        } else if (discount.compareTo(FIFTEEN_PERCENTS) > 0) {
            return YELLOW_COLOR;
        } else if (discount.compareTo(ZERO_PERCENTS) > 0) {
            return RED_COLOR;
        } else {
            return DEFAULT_COLOR;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private void printBonus(String text) {
        final JspWriter out = pageContext.getOut();
        try {
            out.write(text);
        } catch (IOException e) {
            LOG.error(e, e.getCause());
        }
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

}
