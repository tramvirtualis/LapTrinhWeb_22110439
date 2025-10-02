# Registration and Login Test Steps

## Test Steps:

1. **Start the application**
   ```bash
   mvn spring-boot:run
   ```

2. **Check the console logs** for:
   - DataInitializer messages about creating/updating default users
   - Password encoding information

3. **Test Registration**:
   - Go to: http://localhost:8080/register
   - Register a new user (e.g., username: "testuser", password: "TestPass123")
   - Check console for user creation logs

4. **Test Login**:
   - Go to: http://localhost:8080/login
   - Try logging in with the newly registered user
   - Check console for authentication debug logs

5. **Expected Console Output**:
   ```
   === Authentication Debug ===
   Username: testuser
   User found: testuser
   User role: CUSTOMER
   Input password length: 11
   Stored password length: 60
   Stored password starts with: $2a$10$...
   Is BCrypt format: true
   Password matches: true
   === End Debug ===
   ```

## Common Issues to Look For:

1. **Unencoded passwords**: If "Is BCrypt format: false", the password wasn't encoded
2. **Password mismatch**: If "Password matches: false", there's an encoding/comparison issue
3. **User not found**: Check if registration actually saved the user
4. **Database issues**: Check if the database connection is working

## Fix Applied:

- Added comprehensive debugging to UserService
- Enhanced DataInitializer to fix existing users with unencoded passwords
- Added error handling for authentication process
