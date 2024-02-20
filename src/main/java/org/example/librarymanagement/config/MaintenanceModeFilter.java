package org.example.librarymanagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.example.librarymanagement.common.Global;
import org.example.librarymanagement.exception.dto.ApiExceptionResponse;
import org.example.librarymanagement.service.implement.MaintenanceModeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ResourceBundle;

@AllArgsConstructor
@Component
public class MaintenanceModeFilter extends OncePerRequestFilter {

    private MaintenanceModeServiceImpl maintenanceModeService;
    private ResourceBundle resourceBundle;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (maintenanceModeService.isEnabled() && authentication == null) {
            responseException(response);
            return;
        }

        if (maintenanceModeService.isEnabled() && isCurrentUserRole()) {
            responseException(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void responseException(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ApiExceptionResponse api = new ApiExceptionResponse(resourceBundle.getString(
                "system.maintenance-mode.enable"),
                "system.maintenance-mode.enable");

        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(api));
    }

    private boolean isCurrentUserRole() {
        boolean isTrue = Global.getCurrentLogin(resourceBundle).getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
        return isTrue;
    }
}
