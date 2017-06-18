# S.I.D.E
The Simple IDE for a Modern Age
<h1>Features</h1>
<ul>
<li>
Integrated support for [SIL](https://github.com/rjhunjhunwala/S.I.L.O.S)
</li>
<li>Extensible support for arbitrary languages </li>
<li>Customisable Syntax highlighting</li>
<li>A default dark theme (because it looks better)</li>
</ul>
<h1>This repo is a collaboration with Beta - Decay </h1>
https://github.com/beta-decay/Esolang-IDE

<h1>How to add a new language</h1>
You must Provide the following
<ul>
<li>- A command i.e "lang -args foo.lang" (note no trailing space) which will then be passed a file name as a CLA and runs the given file as a program in LANGUAGENAME</li>
</ul>
<h2> You may optionally provide the following<h2>
<ul>
<li> A file named LANGUAGENAME.COLORS.txt which is formatted like so </li>
<ul>
<pre>
Regex 
r g b values of a Color if line matches regex
Regex 
Color if line matches regex 
Regex 
Color if line matches regex 
END_LINES 
Regex 
Color if word matches regex 
Regex 
Color if word matches regex 
</pre>
- A file named LANGUAGENAME.DOCS.txt which is formatted like so
<pre>
Regex
Help text to display if the current line matches the regex "NEWLINE" is used as an escape sequence to represent a new line being displayed.
Regex
Help text to display if the current line matches the regex
Regex
Help text to display if the current line matches the regex
</pre>
Lastly, (this is the part that matters) you must add your language's name and command on seperate lines into the languages.txt file
<pre>
lang1
command1
lang2
command2
lang3
command3
etc...
</pre>
Once you do this, feel free to commit your interpreter to the repository. Support for your language has now been seamlessly integrated into this IDE.
<hr/>
#Screenshots

##Light Theme

![alt tag](https://raw.githubusercontent.com/rjhunjhunwala/S.I.D.E/master/Screenshot.png)

##ark Theme

![alt tag](https://raw.githubusercontent.com/rjhunjhunwala/S.I.D.E/master/LightTheme.png)
