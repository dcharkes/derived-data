module WeblabGradeIceDustWebDSL

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
        grade = 7.0
      },
      practicalAlice{
        answer = "Great"
        grade = 8.0
      }
  }
  mathBob: Submission {
    children =
      examBob{
        answer = "Bad"
        grade = 3.0
      },
      practicalBob{
        answer = "Perfect"
        grade = 10.0
      }
  }
