# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```
This is my first time working with Panache repositories, and based on my understanding, the codebase already follows a well-defined architecture where repositories are isolated within adapter layers and accessed through ports.
Given that, the refactoring I considered was a small, incremental improvement
1. Introducing encapsulation in the existing DbWarehouse entity. 
This helps prevent accidental mutation and reduces coupling, while keeping the overall architectural approach unchanged.

```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```
Open API :::::

While I haven’t worked directly with OpenAPI before,
I understand its purpose in defining and generating API contracts. 
 
Pros :
 1.Open API is less boilerplate code , since it is well
defined contract with external teams. 
 2. Reduces accidental changes.
 3. Clear Api definition.

Cons : 
1. For simpler smaller changes may be creates overhead.
2. Requires a good practice to keep changes or specs up to date.

Code First :::: 
Pros :
1. In my experience, implementing endpoints directly at the controller level has been faster and more intuitive.
2. Easy to implement.
3. less overhead

Cons : 
1. Risk of inconsistency with API's since it is not well defined
2. It will be difficult for consumers to rely on.

I would like to maintain consistency in the approach for the module. If the API's are meant to be consumed by external systems
then I would go with OpenAPI for all endpoints. 
```
----
3. Given that you have limited time and resources for implementing tests for this project, what would be your approach/plan implementing those? Why?

**Answer:**
```
Given the time constraint , 
1. I would first prioritise writing unit test cases for "Business rules", which are core behaviour of
the application like , Create, Replace and Archive. If these tests pass I can ensure the system is reliable.
2. Then I would take up edge cases or not found cases , since some negative scenarios also needs to be verified.
3. Also would do one happy testing for each enpoint, confirming one end to end flow.

```