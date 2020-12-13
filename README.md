![GridFlow Logo](src/resources/logo.png)
---
GridFlow is a power grid simulation focused on energy flow. This is a Cal Poly Capstone project in partnership with Vandenberg Air Force Base. 

## Install & Run
These instructions are for IntelliJ IDEA
1. Clone the repository.
2. Launch IntelliJ and choose open/import project. Pick the gridflow folder you just cloned.
3. Download the [JavaFX Windows SDK](https://gluonhq.com/products/javafx/).
4. In IntelliJ, navigate to File > Project Structure > Libraries.
5. Click the + button and select Java. Select javafx-sdk-x.x.x/lib. Press Apply and OK.
7. Build the project.
8. Create a new Application runtime configuration. Name this gridflow and select application.GridFlowApp as the Main class.
9. Add this as VM options for the runtime configuration:
   >--module-path YOUR-PATH-TO-JAVAFX-SDK\lib --add-modules javafx.controls,javafx.fxml
10. Press Run.
