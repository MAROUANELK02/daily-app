<h2>Running the Application Locally with Docker Compose</h2>

<h3>Prerequisites</h3>
<p>Ensure you have Docker installed on your PC.</p>

<h3>Configuration</h3>
<p>Before launching the application, you need to configure the following in the <code>docker-compose.yml</code> file:</p>

<ul>
  <li>Replace the placeholders with your actual email credentials:
    <pre><code>SPRING_MAIL_USERNAME: your_email_address
SPRING_MAIL_PASSWORD: your_email_key</code></pre>
    <p><em>Tip:</em> You can generate an API key for Gmail to use as the email password.</p>
  </li>
  <li>(Optional) Change the database password and JWT secret:
    <pre><code>MYSQL_ROOT_PASSWORD: your_database_password
JWT_SECRET: your_jwt_secret</code></pre>
  </li>
</ul>

<h3>Launching the Application</h3>
<ol>
  <li>Open your terminal and navigate to the root directory of the project.</li>
  <li>Run the following command to build and start the application:
    <pre><code>docker-compose up --build -d</code></pre>
  </li>
</ol>

<h3>Accessing the Application</h3>
<p>Once the application is running, open your browser and go to <a href="http://localhost:80">http://localhost:80</a> to access the platform.</p>
