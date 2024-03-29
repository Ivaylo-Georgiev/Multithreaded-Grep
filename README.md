
# Multithreaded Grep

Всяка операционна система предлага функционалност за търсене на низ във файлове: като например конзолната команда [`grep`](https://en.wikipedia.org/wiki/Grep) в Unix/Linux. 

В едно директорно дърво може да има:

1.  поддиректории, на произволна дълбочина на влагане
2.  текстови файлове

Целта на програмата е да намери всички срещания на даден символен низ в текстовите файлове в това дърво.

За всяко такова срещане, на конзолата или в определен файл **(в зависимост от подадените параметри)** трябва да се изведе ред в следния формат:

```
path:line_number:line_text
```

където:
-   `path` е относителният път на файла спрямо началната директория
-   `line_number` е номерът на реда във файла, като броенето започва от 1
-   `line_text` е съдържанието на реда, включващ търсения низ

С цел ефективност, решението е многонишково, т.е. няколко паралелно изпълняващи се нишки си разпределят работата, като се синхронизират по подходящ начин, така че резултатът да е коректен.

Всяка `grep` команда има следния вид:

**grep** *[-w|-i] [**string_to_find**] [**path_to_directory_tree**] [**number_of_parallel_threads**] *[path_to_output_file]

1.  _-w|-i_ - **параметри по избор**
    -   -w - указва на `grep` командата да търси само цели думи (т.е "hi" няма да `match`-не "hippo", но ще `match`-не "hi there")
    -   -i - указва на `grep` командата да игнорира `case sensitivity` (т.е "hi" e същото като "Hi")
2.  _string_to_find_ - търсения низ
3.  _path_to_directory_tree_ - път до корена на директорното дърво
4.  _number_of_parallel_threads_ - максималния брой паралелно изпълняващи се нишки
5.  _path_to_output_file_ (**параметър по избор**) - път до файл, в който да се запише изходът от изпълнението на командата `grep`. Ако този параметър не е указан, тогава изходът от програмата трябва да се изведе на конзолата.

*параметри по избор

