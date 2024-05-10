
# Outline
* This solution to the given banking system problem is implemented in Java using the Spring framework.
* To ensure clear separation of concerns, I utilized the Spring framework to move some logic into different classes and packages, implementing the following layers:
    * Controller
    * Service
    * Repository
    * Data Transfer Objects (DTO)
    * Configurations (Beans)
* I have retained some initial logic such as JDBC & SQL queries but commented them out to return dummy logic. This was done to avoid the necessity of implementing full instrumentation, such as:
    * Adding database dependencies like H2
    * Defining a valid database schema
    * Defining valid database connection properties
    * I understand that messaging via SNS is a core requirement of this task.
        * I have also retained this functionality, but messaging has been abstracted by introducing an interface that can have multiple implementations.
            * SNS serves as the default implementation, but it can easily be replaced by any other messaging service.
        * Since I don't have valid AWS configs & credentials, it will always fail.
        * Additionally, I have made the message publishing safe by wrapping it in a try-catch block.
            * This is because if the message fails to publish, the transaction should not fail. At the very least, there is some logging to indicate that the message failed to publish.
