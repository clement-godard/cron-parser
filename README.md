# Cron Parser

Command line application which, given a cron string it parses and expands each field to its final values.

## Instructions

Please find below commands that can be useful when working on this project.

- `gradle compileJava` to compile
- `gradle generateJar` to generate a jar file. See it in `build/libs/cron.jar`
- `java -jar build/libs/cron.jar "1-5 4 */6 */4 */1 /bash"` to run the script
- `gradle clean test` to run test and `open /Users/torgeadelin/Desktop/cron/build/reports/tests/test/index.html`
- `gradle jacocoTestReport` to run coverage tests and `open build/reports/jacoco/test/html/index.html` to open the report

## Cron Rules

1. **Base** rule `"n"` expands to "n"
2. **Step** rule `"*/n"` expands to "0 n 2n 3n ..."
3. **Star** rule `"*"` expands to "0 1 2 3 4 ..." (all possible values)
4. **Sequence** rule `"x,y,z"` expands to "x y z"
5. **Range** rule `"n-m"` expands to "n n+1 n+2 ... m" where n < m

## Examples

- `java -jar build/libs/cron.jar "1 1 1 1 1 /bash"` = At 01:01 on day-of-month 1 and on Monday in January `/bash` will be run

- `java -jar build/libs/cron.jar "1 2 3 4 5 /ls`= At 02:01 on day-of-month 3 and on Friday in April `/ls` will be run
