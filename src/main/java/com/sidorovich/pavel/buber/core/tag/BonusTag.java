package com.sidorovich.pavel.buber.core.tag;

import com.sidorovich.pavel.buber.api.model.Bonus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ResourceBundle;

public class BonusTag extends TagSupport {

    private static final Logger LOG = LogManager.getLogger(BonusTag.class);

    private static final String DEFAULT_COLOR = "bg-dark";
    private static final double SIXTY_PERCENTS = 60d;
    private static final double FOURTY_PERCENTS = 40d;
    private static final double FIFTEEN_PERCENTS = 15d;
    private static final double ZERO_PERCENTS = 0d;
    private static final String GREEN_COLOR = "bg-success";
    private static final String BLUE_COLOR = "bg-primary";
    private static final String YELLOW_COLOR = "bg-warning";
    private static final String RED_COLOR = "bg-danger";
    private static final String EXPIRY_DATE_KEY = "label.card.expiryDate";
    private static final String DESCRIPTION_KEY = "label.card.description";
    private static final String CLASS_ATTR_NAME = "class";
    private static final String DIV_TAG = "div";
    private static final String DIV_CLASS_VALUE = "card %s shadow";
    private static final String H4_TAG = "h4";
    private static final String STYLE_ATTR = "style";
    private static final String H4_CLASS_VALUE = "card-title text-center";
    private static final String H4_STYLE_VALUE = "font-size: 100px";
    private static final String DISCOUNT_FORMAT = "%.0f%%";
    private static final String UL_TAG = "ul";
    private static final String UL_CLASS_VALUE = "list-group list-group-flush";
    private static final String LI_TAG = "li";
    private static final String LI_CLASS_VALUE = "list-group-item";

    private Bonus bonus;
    private LocalizationContext localizationContext;

    @Override
    public int doStartTag() throws JspException {
        ResourceBundle resourceBundle = localizationContext.getResourceBundle();
        final Double discount = bonus.getDiscount();
        final String cardColor = getCardColor(discount);

        try {
            Document doc = getDocument(resourceBundle, discount, cardColor);

            transformDocInfoStream(doc);
        } catch (ParserConfigurationException e) {
            LOG.error(e);
        }

        return SKIP_BODY;
    }

    private Document getDocument(ResourceBundle resourceBundle, Double discount, String cardColor)
            throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder()
                              .newDocument();

        Element div = createDiv(cardColor, doc);
        Element h4 = createHeader(discount, doc);
        Element ul = createList(doc);
        Element info = createInfo(resourceBundle, discount, doc);
        Element expiresAt = createExpiryDate(resourceBundle, doc);

        ul.appendChild(info);
        ul.appendChild(expiresAt);
        div.appendChild(h4);
        div.appendChild(ul);
        doc.appendChild(div);

        return doc;
    }

    private void transformDocInfoStream(Document doc) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        try (StringWriter output = new StringWriter()) {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(output));
            printBonus(output.toString());
        } catch (IOException | TransformerException e) {
            LOG.error(e);
        }
    }

    private Element createExpiryDate(ResourceBundle resourceBundle, Document doc) {
        Element expiresAt = doc.createElement(LI_TAG);

        expiresAt.setAttribute(CLASS_ATTR_NAME, LI_CLASS_VALUE);
        expiresAt.setTextContent(String.format(resourceBundle.getString(EXPIRY_DATE_KEY), bonus.getExpireDate()));

        return expiresAt;
    }

    private Element createInfo(ResourceBundle resourceBundle, Double discount, Document doc) {
        Element info = doc.createElement(LI_TAG);

        info.setAttribute(CLASS_ATTR_NAME, LI_CLASS_VALUE);
        info.setTextContent(String.format(resourceBundle.getString(DESCRIPTION_KEY), discount));

        return info;
    }

    private Element createList(Document doc) {
        Element ul = doc.createElement(UL_TAG);

        ul.setAttribute(CLASS_ATTR_NAME, UL_CLASS_VALUE);

        return ul;
    }

    private Element createHeader(Double discount, Document doc) {
        Element h4 = doc.createElement(H4_TAG);

        h4.setAttribute(STYLE_ATTR, H4_STYLE_VALUE);
        h4.setAttribute(CLASS_ATTR_NAME, H4_CLASS_VALUE);
        h4.setTextContent(String.format(DISCOUNT_FORMAT, discount));

        return h4;
    }

    private Element createDiv(String cardColor, Document doc) {
        Element div = doc.createElement(DIV_TAG);

        div.setAttribute(CLASS_ATTR_NAME, String.format(DIV_CLASS_VALUE, cardColor));

        return div;
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

    public void setLocalizationContext(LocalizationContext localizationContext) {
        this.localizationContext = localizationContext;
    }

}
