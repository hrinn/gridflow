![GridFlow Logo](src/resources/logo.png)
---
GridFlow is a power grid builder and simulator focused on energy flow.
This is a Cal Poly Capstone project in partnership with Vandenberg Air Force Base. 
For a detailed behavior and usage description, please visit the [Wiki](https://github.com/hrinn/gridflow/wiki).

![Sample Grid Image](src/resources/gridsnip.PNG)

## Installation
1. Navigate to the [latest release](https://github.com/hrinn/gridflow/releases/tag/v1.1).
2. Download and extract GridFlowInstaller.zip.
3. Run the installer.
4. Run the application from the Desktop or Start menu shortcut.
5. **If the application fails to run, re-run it as an administrator.** This is likely because it could not create the Credentials.json file in Program Data/GridFlow. You will only have to do this on first launch.

## Getting the Source Code
These instructions are for IntelliJ IDEA
1. Clone the repository.
2. Launch IntelliJ and choose open/import project. Pick the gridflow folder you just cloned.
3. Download the [JavaFX Windows SDK](https://gluonhq.com/products/javafx/).
4. Download the [Jackson API Jar Files](https://drive.google.com/file/d/1GhfWgj3reuj3PYdLCcUk4niAHKcEgayb/view?usp=sharing)
5. In IntelliJ, navigate to File > Project Structure > Libraries.
6. Click the + button and select Java. Select javafx-sdk-x.x.x/lib. Press Apply and OK.
7. Click the + button and select Java. Select folder with Jackson API Jar Files. Press Apply and OK.
8. Build the project.
9. Create a new Application runtime configuration. Name this gridflow and select application.GridFlowApp as the Main class.
10. Add this as VM options for the runtime configuration:
   >--module-path YOUR-PATH-TO-JAVAFX-SDK\lib --add-modules javafx.controls,javafx.fxml
11. Press Run.

## Credits
### Programming Team
* Connor McKee [(@Connor1McKee)](https://github.com/Connor1McKee)
* Iñaki Madrigal [(@inaki332)](https://github.com/inaki332)
* Davis Reschenberg [(@dreschen)](https://github.com/dreschen)
* Hayden Rinn [(@hrinn)](https://github.com/hrinn)

![Programming Team Members](src/resources/team.jpg)

### Graphic Designer
* Bruce Benjamin  [(@bwbenjamin)](https://github.com/bwbenjamin)

### Special Thanks
* Lefty Pakulski, our project client.
* Dr. John Oliver, our Capstone professor.