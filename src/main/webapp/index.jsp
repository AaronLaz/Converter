<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="converter.ConverterEjbBean" %>
<!DOCTYPE html>
<html>
<head>
    <title>Converter</title>
</head>
<body>
<h1>Converter</h1>
<jsp:useBean class="converter.ConverterEjbBean" id="beanConv"/>
<%@page import="java.util.*" %>
<% if(request.getParameter("convert")==null){ %>
<p>Enter an amount to convert: </p>
<form method="get" action="index.jsp">
    <label for="amount">Amount: </label>
    <input type="text" id="amount" name="amount" value="0" size="25"><br>
    <label for="currency">Currency: </label>
    <input type="text" id="currency" name="currency" value="USD" size="25"><br>
    <input type="submit" value = "Submit"><input type="reset" value="Reset">
    <input type="hidden" name = "convert" value="1">
</form>
<% } else{
 double amount = Double.parseDouble(request.getParameter("amount"));
    String currency = request.getParameter("currency");
    amount = beanConv.euroToOtherCurrency(amount,currency);
    out.println("<h4>Le montant converti est : </h4>"+amount+" "+currency);
} %>
<br/>
<a href="hello-servlet">Hello Servlet</a>
</body>
</html>