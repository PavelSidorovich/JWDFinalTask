package com.sidorovich.pavel.buber.core.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.*;

public class TransactionsTag extends TagSupport {

    private static final Logger LOG = LogManager.getLogger(TransactionsTag.class);

    private static final String TITLE_TAG = "<p>%s</p>";
    private static final String LIST_TAG = "<ul class=\"list-unstyled mt-3 mb-4\">%s</ul>";
    private static final String LIST_ELEMENT_TAG = "<li>%c%s %s</li>";

    private String title;
    private Character symbol;
    private List<BigDecimal> transactions;
    private String currency;

    @Override
    public int doStartTag() throws JspException {
        final String titleFormatted = format(TITLE_TAG, title);
        final StringBuilder listElements = new StringBuilder();

        transactions.forEach(element -> listElements.append(
                format(LIST_ELEMENT_TAG, symbol, element, currency))
        );

        final String listFormatted = format(LIST_TAG, listElements);

        printTransactions(titleFormatted + listFormatted);

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private void printTransactions(String text) {
        final JspWriter out = pageContext.getOut();
        try {
            out.write(text);
        } catch (IOException e) {
            LOG.error(e, e.getCause());
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSymbol(Character symbol) {
        this.symbol = symbol;
    }

    public void setTransactions(List<BigDecimal> transactions) {
        this.transactions = transactions;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
