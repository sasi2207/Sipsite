package com.uk.uk.implementation;

import com.uk.uk.repository.AuthRepository;
import com.uk.uk.entity.AuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonUtil {

    @Autowired
    private AuthRepository AuthRepository;

    public Boolean isAdminUser(String emailId) {
        Boolean isAdminUser = false;

        AuthEntity adminUserDetails = AuthRepository.isAdminUserByEmailId(emailId);

        if (null != adminUserDetails)
            isAdminUser = true;

        return isAdminUser;
    }

    public void killDriversProcessesWindows(String driverName) {
        try {
            // Execute taskkill command to kill all geckodriver processes
            Process process = Runtime.getRuntime().exec("taskkill /F /IM " + driverName + ".exe /T" );
            process.waitFor();
            System.out.println(driverName + " processes killed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
