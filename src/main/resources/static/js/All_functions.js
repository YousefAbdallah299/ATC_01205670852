var activeStudentsArray = JSON.parse(localStorage.getItem("data")) || [];


function find(array, studentData) {
    for (var i = 0; i < array.length; i++) {
        if (array[i].id == studentData.id || array[i].email == studentData.email) {
            return array[i];


        }
    }
   
    return false;
}


function check_level(){
    var level = document.getElementById("level").value;
    if (level < 3) {
        document.getElementById("dep").value = "General";
        document.getElementById("dep").disabled = true;

    } else {
        document.getElementById("dep").disabled = false;

    }
}
window.onload = check_level;


function savedata() {
    var id = document.getElementById("id").value;
    var Fname = document.getElementById("Fname").value;
    var Lname = document.getElementById("Lname").value;
    var date = document.getElementById("date").value;
    var gpa = document.getElementById("gpa").value;
    var gender = document.getElementById("gender").value;
    var level = document.getElementById("level").value;
    var status = document.getElementById("status").value; 
    var email = document.getElementById("email").value;
    var phone = document.getElementById('phone').value;
    var department = document.getElementById("dep").value;

    var studentData = {
        id: id,
        Fname: Fname,
        Lname: Lname,
        date: date,
        gpa: gpa,
        gender: gender,
        level: level,
        status: status,
        level: level,
        email: email,
        phone: phone,
        department: department
    };
    
   
    const pattern = /\S+@\S+\.\S+/;
    if (studentData.id != "" && studentData.Fname != "" && studentData.Lname != "" &&
        studentData.date != "" && studentData.email != "" && studentData.gpa != "" &&
        (studentData.gpa < 4 && studentData.gpa > 0) && pattern.test(studentData.email)) {
        if (find(activeStudentsArray, studentData)) {
            alert("this student has been added before , plz check id and email again");
        } else {
            activeStudentsArray.push(studentData);

            window.localStorage.setItem("data", JSON.stringify(activeStudentsArray));
            alert("New student has been added");
        }
        
    }
    
}



function SignIn() {
    var id = document.getElementById("id").value;
    var email = document.getElementById("email").value;

    var user = {
        id: id,
        email : email
    }
   
    var regestired = Object.assign({}, find(activeStudentsArray, user));

    if (user.id != '' && user.email != '') {
        if (find(activeStudentsArray, user)) {

            if (user.id != regestired.id || user.email != regestired.email) {
                alert("No Matching between ID and Email , plz try again");
            } else {

                window.location.href = 'Update_Info.html';
                window.localStorage.setItem("regestired", JSON.stringify(regestired));


            }
        } else {
            alert("There is no user with this data, plz try again");
        }
    }
}



