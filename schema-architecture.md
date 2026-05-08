This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.

---

1. User accesses AdminDashboard or Appointment pages.
2. The action is routed to the appropriate Thymeleaf or REST controller.
3. The controller calls the service layer
4. The Service Layer coordinates business logic and interacts with JPA/MySQL Repositories.
5. Repositories execute queries against the MySQL Database to persist or retrieve data.
6. The database results are mapped back into Entities (e.g., Patient, Doctor, Appointment, Admin).
7. These Entities are then processed by the service and passed back to the Controller for display or API response.