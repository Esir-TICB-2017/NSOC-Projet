 function onSignIn(googleUser) {
        // Useful data for your client-side scripts:
        var profile = googleUser.getBasicProfile();

        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;

        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8080/login');
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onload = function() {
        };
        xhr.send('idtoken=' + id_token);
      };

      function callback(data) {
        console.log(JSON.parse(data));
      }

     function signOut() {
            var auth2 = gapi.auth2.getAuthInstance();
            auth2.signOut().then(function () {
               console.log('User signed out.');
              var xmlHttp = new XMLHttpRequest();
                     xmlHttp.open("GET", "/logout", true); // true for asynchronous
                      xmlHttp.send(null);

            });
          }