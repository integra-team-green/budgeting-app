package cloudflight.integra.backend.security;

import cloudflight.integra.backend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userDetailsService;
    private final JwtUtils jwtUtils;

    public JwtRequestFilter(UserService userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Main filter method that executes once per request This filter intercepts all incoming HTTP
     * requests and checks for JWT tokens in the Authorization header to authenticate users
     *
     * @param request - the HTTP request object
     * @param response - the HTTP response object
     * @param chain - the filter chain for passing the request to the next filter
     * @throws ServletException if there's a servlet error
     * @throws IOException if there's an I/O error
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Extract the Authorization header from the request
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Initialize variables for username and JWT token
        String username = null;
        String jwt = null;

        // Check if the header exists and has the Bearer token format
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            // Extract the token (remove "Bearer " prefix)
            jwt = header.substring(7);
            try {
                // Extract the username from the token
                username = jwtUtils.extractUsername(jwt);
            } catch (Exception e) {
                // Log any errors during token parsing
                logger.error("Error extracting username from token", e);
            }
        }

        // Only proceed if we have a username and no authentication is already set
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the token against the user details
            if (jwtUtils.validateToken(jwt, userDetails)) {
                // Create authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Add request details to the authentication
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in the security context
                // This tells Spring Security the request is authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }
}
