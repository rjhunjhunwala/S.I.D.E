# S.I.D.E
### The Simple IDE for a Modern Age

The intent of this IDE is to provid a streamlined environment for development across different languages, especially esoteric languages
which generally lack developemental tools. 

<h1>Features</h1>
<ul>
<li>
Integrated support for SIL/S.I.L.O.S (https://github.com/rjhunjhunwala/SIL-S.I.L.O.S)
</li>
<li>Extensible support for arbitrary languages </li>
<li>Customisable Syntax highlighting</li>
</ul>
<h1>This repo is a collaboration with Beta - Decay </h1>
https://github.com/beta-decay/Esolang-IDE

<h1>How to add a new language</h1>
One of the core features of the IDE is the ease with which new languages can be added. In order to add a new language please provide the following.
<ul>
<li> A command i.e "lang -args foo.lang" (note no trailing space) which will then be passed a file name as a CLA and runs the given file as a program in LANGUAGENAME</li>
</li>
<li> (Optional) A file named LANGUAGENAME.COLORS.txt which is formatted like the following document:
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
</li>
<li>
(Optional) A file named LANGUAGENAME.DOCS.txt which is formatted like the following:
<pre>
Regex
Help text to display if the current line matches the regex "NEWLINE" is used as an escape sequence to represent a new line being displayed.
Regex
Help text to display if the current line matches the regex
Regex
Help text to display if the current line matches the regex
</pre>
</li>
<li>
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
</li>
</ul>
For examples of this, look at the files in the SIDE folder provided for languages already implemented. Once you do this, feel free to commit your interpreter to the repository. Support for your language has now been seamlessly integrated into this IDE. If you have any questions, feel free to reach out through the issues page.
<hr/>

<h1>Screenshots</h1>

<h2>Dark Theme</h2>

![alt tag](https://raw.githubusercontent.com/rjhunjhunwala/S.I.D.E/master/Screenshot.png)

<h2>Light Theme</h2>

![alt tag](https://raw.githubusercontent.com/rjhunjhunwala/S.I.D.E/master/LightTheme.png)
