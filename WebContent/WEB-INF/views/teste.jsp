<%@ page import="net.tanesha.recaptcha.*"%>
 
<html>
<body>
<form action="process" method="post">
        <%
          //ReCaptcha c = ReCaptchaFactory.newReCaptcha("your_public_key", "your_private_key", false);
        ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(
				"6LfmCzMUAAAAAD98ZaaQ7dNcPjQox1sRfmYgNydx",
				"6LfmCzMUAAAAAEmQZsWi5qBYOUi3TkjGeC627Tbm", false);
		out.print(captcha.createRecaptchaHtml(null, null));
        %>
        <input type="submit" value="submit" />
    </form>
</body>
</html>