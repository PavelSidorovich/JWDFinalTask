package com.sidorovich.pavel.buber.command.impl;

import com.sidorovich.pavel.buber.command.Command;
import com.sidorovich.pavel.buber.command.CommandRequest;
import com.sidorovich.pavel.buber.command.CommandResponse;
import com.sidorovich.pavel.buber.dao.impl.AccountDao;
import com.sidorovich.pavel.buber.model.impl.Account;

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
        final List<Account> accounts = accountDao.readAll();
        request.addAttributeToJsp(ACCOUNTS_ATTRIBUTE_NAME, accounts);
        return FORWARD_TO_ACCOUNTS_PAGE;
    }
}
