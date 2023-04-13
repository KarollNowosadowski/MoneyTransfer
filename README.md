# MoneyTransfer

Demo wersja zawiera kontroler i kilka przypadkow testowych z uzyciem MockMvc


# Pytania na ktore nie udzielilem odpowiedzi podczas rozmowy

## 1. Sql injection
Wlasciwie to nie znalem tego pojecia pod ta nazwa. Aczkolwiek znam roznice pomiedzy PreparedStatement vs Statement, pozwole sobie wkleic swoje notatki sprzed kilku miesiecy na ten temat:

The biggest disadvantage of `Statement` is security vulnerability.  
When we give an user full control over our query, he can do something like that:
` String query = "SELECT * FROM confidential_data WHERE username = '' OR '1'='1'" `
and this query will return all the rows from the confidential_data table because '1'='1' is always true.
We cannot allow such situations to occur, the solution to this issue is `PreparedStatement`
When using prepared statements, the database driver will handle the task of properly escaping the input values so that 
they are treated as literals and not interpreted as part of the SQL query. 
So let's consider that on example:
```
String query = "SELECT * FROM confidential_data WHERE username = ? 
String fakeUsername = "'' OR '1'='1'";
PreparedStatement statement = connection.prepareStatement(query);
statement.setString(1, fakeUsername); 
```
This query will be interpreted as:
` SELECT * FROM confidential_data WHERE username = "'' OR '1'='1'" `
So only the row/rows with username equal to ` '' OR '1'='1' ` will be returned 
because the parameter is treated as literal, not as "legal" part of the query

PreparedStatement pozwala nam uniknac luki w bezpieczenstwie pwszechnie nazywanej SQL injection

## 2. Hibernate validation
Walidacje Hiberneta moga byc uzyte do upewnienia się, ze dane są we wlasciwym formacie, np. `@NotNull` sprwadza czy dane pole nie jest null-em, `@Min` `@Max` moga zostac uzyte do okreslenie limitow wartosci. Mamy tez mozliwosc do walidacji dat za pomoca m.in. `@Past`, `@FutureOrPresent`.

Na rozmowie wspominalem o uzyciu `@NotBlank` i `@Valid` ktore rowniez zaliczaja sie do tego pytania. Mozemy wiec stworzyc DTO z polem:

```
@NotBlank(message = "Name must contain non-whitespace characters")
private String name;
```

a nastepnie podczas mapowania requestBody na DTO uzyc adnotacji `@Valid`
```
foo(@RequestBody @Valid CustomDTO dto)
```

zapewni to ze walidacje zostana zastosowane podczas mapowania na DTO.
