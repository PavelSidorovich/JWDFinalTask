package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.core.dao.AccountDao;
import com.sidorovich.pavel.buber.api.model.Account;

import java.util.List;

public class ShowAccountPageCommand implements Command {

    private static final String ACCOUNTS_ATTRIBUTE_NAME = "accounts";

    private static final CommandResponse FORWARD_TO_ACCOUNTS_PAGE = new CommandResponse() {
        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return "/WEB-INF/jsp/account.jsp";
        }
    };

    private final AccountDao accountDao;

    public ShowAccountPageCommand(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Account> accounts = accountDao.findAll();
        request.addAttributeToJsp(ACCOUNTS_ATTRIBUTE_NAME, accounts);
        return FORWARD_TO_ACCOUNTS_PAGE;
    }
}
