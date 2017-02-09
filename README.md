# S.I.D.E
The Simple IDE for a Modern Age
<h1>Features</h1>
<ul>
<li>
Integrated support for [SIL](https://github.com/rjhunjhunwala/S.I.L.O.S)
</li>
<li>Extensible support for arbitrary languages (currently only brainf*** is supported (as well as SIL))</li>
<li>Customisable Syntax highlighting</li>
<li>A default dark theme (because it looks better)</li>
</ul>
<h1>This repo is a collaboration with Beta - Decay </h1>
https://github.com/beta-decay/Esolang-IDE

<h1>How to add a new language</h1>
You must Provide the following
- A command i.e "lang -args foo.lang" (note no trailing space) which will then be passed a file name as a CLA and runs the given file as a program in LANGUAGENAME
## You may optionally provide the following
- A file named LANGUAGENAME.COLORS.txt which is formatted like so
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
Lastly, you must add LANGUAGENAME into the String[] called languages in the GUI class
<hr/>
Screenshot
![alt tag](https://raw.githubusercontent.com/rjhunjhunwala/S.I.D.E/master/Screenshot.png)
