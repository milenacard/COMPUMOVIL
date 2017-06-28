const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest((req, res) => {
  // Grab the text parameter.
  const original = req.query.text;
  // Push it into the Realtime Database then send a response
  admin.database().ref('/messages').push({original: original}).then(snapshot => {
    // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
    // res.redirect(303, snapshot.ref);
    res.redirect(303, "https://www.facebook.com/dani.ospina96");
  });
});

// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
exports.makeUppercase = functions.database.ref('/messages/{pushId}/original')
    .onWrite(event => {
      // Grab the current value of what was written to the Realtime Database.
      const original = event.data.val();
      console.log('Uppercasing', event.params.pushId, original);
      const uppercase = original.toUpperCase();
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      return event.data.ref.parent.child('uppercase').set(uppercase);
    });

exports.notificarInteres = functions.database.ref('/Intereses/{pushId}/libro')
      .onWrite(event => {
        const libro = event.data.val();

        //Busca el propietario del libro
        admin.database().ref('/Libros/' + libro + '/propietario').once('value').then(function(snapshot) {
          var propietario = snapshot.val();

          //Busca el token del propietario
          admin.database().ref("Tokens").orderByChild("email").equalTo(propietario).on("child_added", function(snapshot) {
            var token = snapshot.val().token;

            const payload = {
              notification: {
                title: 'Alguien está interesado en tu libro',
                body: ':D',
                sound: 'default',
                // icon: follower.photoURL
              }
            };

            admin.messaging().sendToDevice(token, payload).then(function(response) {
              // See the MessagingDevicesResponse reference documentation for
              // the contents of response.
              console.log("Successfully sent message:", response);
            })
            .catch(function(error) {
              console.log("Error sending message:", error);
            });


          });
        });

      });


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
