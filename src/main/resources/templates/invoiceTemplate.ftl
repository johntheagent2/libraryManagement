<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Email Confirmation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body style="font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; color: #333;">

<header style="background-color: #2c3e50; color: #fff; padding: 20px; text-align: center;">
    <h1 style="color: #3498db;">Invoice</h1>
</header>

<section>
    <h2>Customer Information:</h2>
    <p>Customer's name: ${email}</p>
</section>

<table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
    <thead>
    <tr>
        <th style="border: 1px solid #ddd; padding: 12px; text-align: left; background-color: #f2f2f2;">Title</th>
        <th style="border: 1px solid #ddd; padding: 12px; text-align: left; background-color: #f2f2f2;">Price</th>
    </tr>
    </thead>
    <tbody>
    <#list books as book>
        <tr>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: left;">${book.title}</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: left;">${book.price}</td>
        </tr>
    </#list>
    </tbody>
</table>

<section>
    <h3 align="right">Total Price: ${total}</h3>
</section>
<section>

</section>
</body>
</html>