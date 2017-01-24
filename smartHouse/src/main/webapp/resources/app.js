 function onSignIn(googleUser) {
        // Useful data for your client-side scripts:
        var profile = googleUser.getBasicProfile();

        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;

        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8080/tokensignin');
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onload = function() {
        };
        xhr.send('idtoken=' + id_token);
      };

      function doTest(userid) {

        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", "api/testGet", true); // true for asynchronous
        xmlHttp.setRequestHeader("Authorization", getCookie('userId'));

          xmlHttp.send(null);
      }

      function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

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