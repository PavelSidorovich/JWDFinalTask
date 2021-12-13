package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
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
import static org.testng.Assert.*;

public class BlockUserCommandTest {

    @Mock
    private CommandRequest commandRequest;

    @Mock
    private UserService userService;

    @Mock
    private RequestFactory requestFactory;

    @InjectMocks
    private BlockUserCommand command;

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

        when(commandRequest.getParameter("id")).thenReturn("1");
        when(userService.findById(1L)).thenReturn(user);

        command.execute(commandRequest);

        verify(userService).update(user.get().withStatus(UserStatus.ACTIVE));
        verify(requestFactory).createJsonResponse(null, JsonResponseStatus.SUCCESS,
                                                     "User status was successfully updated!");
    }

}