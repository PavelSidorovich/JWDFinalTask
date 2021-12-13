package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class AccountControlCommandTest {

    @Mock
    private CommandRequest commandRequest;

    @Mock
    private UserService userService;

    @Mock
    private RequestFactory requestFactory;

    @InjectMocks
    private AccountControlCommand command;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void execute_shouldCreateForwardResponse_always() {
        Optional<BuberUser> user = Optional.of(
                BuberUser.with()
                         .account(new Account(1L, "+375 29 111-11-11",
                                              "1234", Role.CLIENT)
                         )
                         .firstName("Pavel")
                         .lastName("Sidorovich")
                         .cash(BigDecimal.ZERO)
                         .status(UserStatus.BLOCKED)
                         .build()
        );

        when(commandRequest.retrieveFromSession("user")).thenReturn(
                Optional.of(new Account("+375 29 111-11-11", "1234", Role.CLIENT))
        );
        when(userService.findByPhone("+375 29 111-11-11")).thenReturn(user);

        command.execute(commandRequest);

        verify(commandRequest).addAttributeToJsp("user", user.get());
        verify(requestFactory).createForwardResponse(PagePaths.ACCOUNT_CONTROL.getJspPath());
    }

}