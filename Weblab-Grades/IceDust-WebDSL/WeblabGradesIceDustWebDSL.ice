module WeblabGradesIceDustWebDSL

model

  entity Submission {
    answer     : String?
    grade      : Float?  = if(childPass) childGrade else no value (default)
    pass       : Boolean = grade >= 5.5 && childPass <+ false
    childGrade : Float?  = avg(children.grade)
    childPass  : Boolean = conj(children.pass)
  }

  relation Submission.parent ? <-> * Submission.children

data

  mathAlice: Submission {
    children =
      examAlice{
        answer = "Good"
        grade = 8.0
      },
      labAlice{
        children =
          lab1Alice{
            answer = "Perfect"
            grade = 10.0
          },
          lab2Alice{
            answer = "Sufficient"
            grade = 6.0
          }
      }
  }
  mathBob: Submission {
    children =
      examBob{
        answer = "Very Good"
        grade = 9.0
      },
      labBob{
        children =
          lab1Bob{
            answer = "Insufficient"
            grade = 3.0
          },
          lab2Bob{
            answer = "Perfect"
            grade = 10.0
          }
      }
  }
