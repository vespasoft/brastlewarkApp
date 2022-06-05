# brastlewarkApp
### Analysis and Development ### 
This is one of these times, our heroes just arrived at a Gnome town called Brastlewark. To
facilitate trade with the local population they need an easy way to browse all the inhabitants
details. We&#39;ve found a server providing all the census data of Brastlewark at the following

This is a single application written in Kotlin that show a citizens list of a Gnome town called Brastlewark. 
The app has been designed for get all local polulation data in an easy way giving an awesome user experience,
And allows users to explore the data with few clicks.

### Architecture ###
This project has developed with the Clean Architecture Pattern uncoupling the code in four layers, this make it easy at the time of mantain, test and scale the code exponentialy.

![Clean Arquitecture Logo](https://res.cloudinary.com/practicaldev/image/fetch/s--T7GIdw6s--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://miro.medium.com/max/1488/1%2AD1EvAeK74Gry46JMZM4oOQ.png "An exemplary image")

*Application's layers*
  * Data
  * Domain
  * UI/ViewModel

### External Libraries and Dependencies ###
 * Coroutines: Has been implemented concurrency design pattern to simplify the code that executes asynchronously. Coroutines help us to manage long-running task that might block the main thread and make your app to become unresponsive.  
 * Flow: It is a type of coroutines that can emit multiple values secuencially, as opposed to suspend functions that return only a single value.
 * Hilt: Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
 * Coil: Has been implemented Coil, An image loading library for Android backed by Kotlin Coroutines
 * ViewModel: It is one of the android architecture components and it has been designed to store UI-related data in the lifecycle of an android application.
 * Mockk: It is a unit test library for Android and allows you to mock objects in both your Android unit tests and instrumented tests.
 * Retrofit: Is the most used type-safe REST client for Android.
 
 ### Design Patterns ###
  * Repository: Has been implemented repository pattern in the data layer, in this way the origin of data have hidden and encapsulated.
  * Dependency Injection: Has been implemented the dependency injection pattern using Hilt framework and complying with **Dependency Inversion Principle**
 
 ### Application Scalability ###
  * Thanks to the architecture  pattern used, each of the layers of the application are completely decoupled and can be easily refactored without affecting the rest. The more layers your code have the more little your classes will be and easier will be refactor and find issues.
  * The best way to comply with the **single responsibility pattern** is stay your code on little classes and little functions, in this way your code never will have more than one responsibility.
  * Bussines logic is cetralized into UseCases classes, in this way is easily to mantain and add new features without need to modify the existing classes, for example: At the moment of add a new feature is as easy as create a new use case called NewFeatureUseCase and extending the use case abstract class, in this way complying with the **open close principle**.
  * A CustomeView class is a good way to reuse code in the view layer, in this case the custom view AnimationCustomView encapsulate the animation logic and the external library used to implemented the animation, in the future, it will be easyer change the lottie library for another one.
  
