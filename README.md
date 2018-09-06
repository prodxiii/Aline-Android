# Aline Android Client
Client for the *Aline* assisted wayfinding mobile application on the Android operating system. 

*Aline* is an intuitive real-time wayfinding and rich-data communication app catering predominantly to the elderly and those lacking extensive familiarity with mobile technology. 
Users can be guided to their desired destinations with real-time assistance from their chosen contacts.

## Features
*Features to be added as they are implemented.*

## Screenshots
*Screenshots to be added as app is developed.*

## Installation
### Prerequisites
* Android Studio

And one of:
* Mobile device running Android version 4.0.3 or later; or
* Intel® device with Intel® VT-x

### Building
Clone the repository and open the project in Android Studio. 
To clone directly from Android Studio, select *File > New > Project from Version Control > GitHub* and enter the repository URL.

The project must be synced with Gradle to successfully build. 
Select *File > Sync Project with Gradle Files* or use the toolbar button. 
Select *Build > Make Project* or use the toolbar button to build the project.

### Running on an Android device via sideload
Select *Build > Build APK(s)* in Android Studio. 
The APK will be located in the directory /app/build/outputs/apk/debug. 
The application can be installed by executing the APK on the mobile device.

#### Note 
To sideload the app, installation from unknown sources must be allowed. 
The process for enabling this option varies by device and operating system version. 
For further information, please contact the device manufacturer.

### Running on an Android device via ADB
Ensure that Developer Options is enabled on the Android device. 
The process for enabling this option varies by device and operating system version. 
For most devices, Developer Options is enabled by opening the Settings menu and navigating to *System > About Phone > Software Info* and tapping *Build number* multiple times in quick succession. 
For further information, please contact the device manufacturer.

Navigate to the Developer Options menu on the Android device and ensure that USB debugging is enabled. 
Connect the Android device via USB to the device running Android Studio. 
Select *Run > Run 'app'* in Android Studio. 
A dialogue box will appear prompting for the deployment target; select the connected mobile device to run the application.

#### Note
If the device does not appear, the OEM USB driver for ADB must be installed. 
Please [download and install the appropriate OEM driver](https://developer.android.com/studio/run/oem-usb#Drivers).

### Running on a virtual device
Select *Run > Run 'app'* in Android Studio. A dialogue box will appear prompting for the deployment target; select a vitual device running API 21 or later must be selected. 
If none exist, create a new virtual device.

#### Note
If an error regarding hardware acceleration is encountered, ensure that Intel® VT-x virtualisation technology is enabled and [Intel® HXAM](https://software.intel.com/en-us/articles/intel-hardware-accelerated-execution-manager-intel-haxm) is installed. 
The option for enabling Intel® VT-x is located in the device BIOS/UEFI menu. 
The process for entering the BIOS/UEFI varies by device. 
For most devices, this can be achieved by holding the F2 key upon boot. 
For further information, please contact the device manufacturer.

## Authors
*Aline* is created by IT Project Cool Group (ITCG), University of Melbourne.

## Licence
© Cool Group 2018. All rights reserved.
