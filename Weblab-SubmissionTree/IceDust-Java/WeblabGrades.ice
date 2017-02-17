module WeblabGrades

model

  entity Student {
    name : String
  }
  
  entity Submission {
    name : String = assignment.name + " " + student.name
  }
  
  entity Assignment {
    name : String
  }
  
  relation Submission.student    1 <-> * Student.submissions
  relation Submission.assignment 1 <-> * Assignment.submissions
  relation Assignment.parent     ? <-> * Assignment.children
  
  relation Submission.parent     ? = assignment.parent.submissions.find(x => x.student == student)
                                   <-> * Submission.children

data

  alice : Student {
    name = "Alice"
    submissions = 
      mathAlice {
        assignment = math
      },
      examAlice {
        assignment = exam
      },
      labAlice {
        assignment = lab
      }
  }
  
  bob : Student{
    name = "Bob"
    submissions = 
      mathBob {
        assignment = math
      },
      examBob {
        assignment = exam
      },
      labBob {
        assignment = lab
      }
  }
  
  math : Assignment {
    name = "Math"
    children =
      exam {
        name = "Exam"
      },
      lab {
        name = "Lab"
      }
  }
  
execute

  "MathAlice child submissions: "
  mathAlice.children.name
  "ExamAlice parent submission: "
  examAlice.parent.name
  "LabAlice parent submission:  "
  labAlice.parent.name
  