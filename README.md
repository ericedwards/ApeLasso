https://johnnysn.hashnode.dev/simple-api-key-authentication-in-spring-boot-31
https://springdoc.org/

Google Search: spring security replacement for AntPathRequestMatcher

In recent versions of Spring Security, specifically from Spring Security 5.8 onwards, the AntPathRequestMatcher has been deprecated and marked for removal in favor of more flexible and robust alternatives. The primary replacement is the requestMatchers() method, which offers a unified way to secure requests.
Key Replacements and Usage:

* requestMatchers() Method:
* This is the recommended replacement for antMatchers(), mvcMatchers(), and regexMatchers().
* It intelligently chooses the most appropriate RequestMatcher implementation based on the classpath: MvcRequestMatcher if Spring MVC is present, otherwise AntPathRequestMatcher.
* Example:

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll() // Matches paths like /public/resource
                    .requestMatchers("/admin/**").hasRole("ADMIN") // Matches paths like /admin/dashboard
                    .anyRequest().authenticated()
                )
                .formLogin()
                .build();
        }

* PathPatternRequestMatcher (for direct usage):
* While requestMatchers() handles most scenarios, if direct instantiation of a RequestMatcher is needed, PathPatternRequestMatcher is the suggested replacement for AntPathRequestMatcher.
* This is particularly relevant when dealing with specific filter configurations or custom security components that require a RequestMatcher instance.
* Example:

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(new PathPatternRequestMatcher("/greet")).permitAll()
                    .anyRequest().authenticated()
                )
                .formLogin()
                .build();
        }

Migration Steps:

* Replace calls to antMatchers(), mvcMatchers(), and regexMatchers() with requestMatchers().
* If you are directly creating AntPathRequestMatcher instances, consider replacing them with PathPatternRequestMatcher.
* Ensure your Spring Security configuration uses authorizeHttpRequests() instead of the deprecated authorizeRequests().

AI responses may include mistakes.

