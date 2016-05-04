application WeblabGradesNonRecursiveIceDustWebDSL

imports lib/icedust/crud-ui

imports lib/icedust/Expressions

section  data

  init
  {
    var examAlice := Submission2{} ;
    var examBob := Submission2{} ;
    var lab1Alice := Submission3{} ;
    var lab1Bob := Submission3{} ;
    var lab2Alice := Submission3{} ;
    var lab2Bob := Submission3{} ;
    var labAlice := Submission2{} ;
    var labBob := Submission2{} ;
    var mathAlice := Submission1{} ;
    var mathBob := Submission1{} ;
    examAlice.answer := "Good";
    examAlice.grade := 8.0;
    examBob.answer := "Very Good";
    examBob.grade := 9.0;
    lab1Alice.answer := "Perfect";
    lab1Alice.grade := 10.0;
    lab1Bob.answer := "Insufficient";
    lab1Bob.grade := 3.0;
    lab2Alice.answer := "Sufficient";
    lab2Alice.grade := 6.0;
    lab2Bob.answer := "Perfect";
    lab2Bob.grade := 10.0;
    labAlice.children.add(lab2Alice);
    labAlice.children.add(lab1Alice);
    labBob.children.add(lab2Bob);
    labBob.children.add(lab1Bob);
    mathAlice.children.add(labAlice);
    mathAlice.children.add(examAlice);
    mathBob.children.add(labBob);
    mathBob.children.add(examBob);
    examAlice.save();
    examBob.save();
    lab1Alice.save();
    lab1Bob.save();
    lab2Alice.save();
    lab2Bob.save();
    labAlice.save();
    labBob.save();
    mathAlice.save();
    mathBob.save();
  }

section  model

  entity Submission1 {
    answer : String ( default= null )
    function getAnswer ( ) : String
    {
      return this.answer;
    }
    static function getAnswer ( en : Submission1 ) : String
    {
      return if ( en == null ) ( null as String ) else en.getAnswer();
    }
    static function getAnswer ( entities : List<Submission1> ) : List<String>
    {
      return [ en.getAnswer() | en : Submission1 in entities where en.getAnswer() != null ];
    }
    childGrade : Float := calculateChildGrade()
    function getChildGrade ( ) : Float
    {
      return this.childGrade;
    }
    static function getChildGrade ( en : Submission1 ) : Float
    {
      return if ( en == null ) ( null as Float ) else en.getChildGrade();
    }
    static function getChildGrade ( entities : List<Submission1> ) : List<Float>
    {
      return [ en.getChildGrade() | en : Submission1 in entities where en.getChildGrade() != null ];
    }
    function calculateChildGrade ( ) : Float
    {
      return Expressions.avg_Float(Submission2.getGrade(Submission1.getChildren(this)));
    }
    childPass : Bool := calculateChildPass()
    function getChildPass ( ) : Bool
    {
      return this.childPass;
    }
    static function getChildPass ( en : Submission1 ) : Bool
    {
      return if ( en == null ) ( null as Bool ) else en.getChildPass();
    }
    static function getChildPass ( entities : List<Submission1> ) : List<Bool>
    {
      return [ en.getChildPass() | en : Submission1 in entities where en.getChildPass() != null ];
    }
    function calculateChildPass ( ) : Bool
    {
      return Expressions.conj(Submission2.getPass(Submission1.getChildren(this)));
    }
    grade : Float ( default= null )
    function getGrade ( ) : Float
    {
      return if ( this.grade != null ) this.grade else this.calculateGrade();
    }
    static function getGrade ( en : Submission1 ) : Float
    {
      return if ( en == null ) ( null as Float ) else en.getGrade();
    }
    static function getGrade ( entities : List<Submission1> ) : List<Float>
    {
      return [ en.getGrade() | en : Submission1 in entities where en.getGrade() != null ];
    }
    function calculateGrade ( ) : Float
    {
      return ( Expressions.conditional_One_One_One(Submission1.getChildPass(this), Submission1.getChildGrade(this), null) as Float );
    }
    pass : Bool := calculatePass()
    function getPass ( ) : Bool
    {
      return this.pass;
    }
    static function getPass ( en : Submission1 ) : Bool
    {
      return if ( en == null ) ( null as Bool ) else en.getPass();
    }
    static function getPass ( entities : List<Submission1> ) : List<Bool>
    {
      return [ en.getPass() | en : Submission1 in entities where en.getPass() != null ];
    }
    function calculatePass ( ) : Bool
    {
      return ( Expressions.choice_One_One(Expressions.and(Expressions.gte_Float(Submission1.getGrade(this), 5.5), Submission1.getChildPass(this)), false) as Bool );
    }
    children : Set<Submission2>
    function getChildren ( ) : List<Submission2>
    {
      return [ en | en : Submission2 in this.children ];
    }
    static function getChildren ( en : Submission1 ) : List<Submission2>
    {
      var empty : List<Submission2> ;
      return if ( en == null ) empty else en.getChildren();
    }
    static function getChildren ( ens : List<Submission1> ) : List<Submission2>
    {
      var r : List<Submission2> ;
      for
      (
      en
      in
      ens
      )
      {
        r.addAll(en.getChildren());
      }
      return r;
    }
  }

  entity Submission2 {
    answer : String ( default= null )
    function getAnswer ( ) : String
    {
      return this.answer;
    }
    static function getAnswer ( en : Submission2 ) : String
    {
      return if ( en == null ) ( null as String ) else en.getAnswer();
    }
    static function getAnswer ( entities : List<Submission2> ) : List<String>
    {
      return [ en.getAnswer() | en : Submission2 in entities where en.getAnswer() != null ];
    }
    childGrade : Float := calculateChildGrade()
    function getChildGrade ( ) : Float
    {
      return this.childGrade;
    }
    static function getChildGrade ( en : Submission2 ) : Float
    {
      return if ( en == null ) ( null as Float ) else en.getChildGrade();
    }
    static function getChildGrade ( entities : List<Submission2> ) : List<Float>
    {
      return [ en.getChildGrade() | en : Submission2 in entities where en.getChildGrade() != null ];
    }
    function calculateChildGrade ( ) : Float
    {
      return Expressions.avg_Float(Submission3.getGrade(Submission2.getChildren(this)));
    }
    childPass : Bool := calculateChildPass()
    function getChildPass ( ) : Bool
    {
      return this.childPass;
    }
    static function getChildPass ( en : Submission2 ) : Bool
    {
      return if ( en == null ) ( null as Bool ) else en.getChildPass();
    }
    static function getChildPass ( entities : List<Submission2> ) : List<Bool>
    {
      return [ en.getChildPass() | en : Submission2 in entities where en.getChildPass() != null ];
    }
    function calculateChildPass ( ) : Bool
    {
      return Expressions.conj(Submission3.getPass(Submission2.getChildren(this)));
    }
    grade : Float ( default= null )
    function getGrade ( ) : Float
    {
      return if ( this.grade != null ) this.grade else this.calculateGrade();
    }
    static function getGrade ( en : Submission2 ) : Float
    {
      return if ( en == null ) ( null as Float ) else en.getGrade();
    }
    static function getGrade ( entities : List<Submission2> ) : List<Float>
    {
      return [ en.getGrade() | en : Submission2 in entities where en.getGrade() != null ];
    }
    function calculateGrade ( ) : Float
    {
      return ( Expressions.conditional_One_One_One(Submission2.getChildPass(this), Submission2.getChildGrade(this), null) as Float );
    }
    pass : Bool := calculatePass()
    function getPass ( ) : Bool
    {
      return this.pass;
    }
    static function getPass ( en : Submission2 ) : Bool
    {
      return if ( en == null ) ( null as Bool ) else en.getPass();
    }
    static function getPass ( entities : List<Submission2> ) : List<Bool>
    {
      return [ en.getPass() | en : Submission2 in entities where en.getPass() != null ];
    }
    function calculatePass ( ) : Bool
    {
      return ( Expressions.choice_One_One(Expressions.and(Expressions.gte_Float(Submission2.getGrade(this), 5.5), Submission2.getChildPass(this)), false) as Bool );
    }
    children : Set<Submission3>
    function getChildren ( ) : List<Submission3>
    {
      return [ en | en : Submission3 in this.children ];
    }
    static function getChildren ( en : Submission2 ) : List<Submission3>
    {
      var empty : List<Submission3> ;
      return if ( en == null ) empty else en.getChildren();
    }
    static function getChildren ( ens : List<Submission2> ) : List<Submission3>
    {
      var r : List<Submission3> ;
      for
      (
      en
      in
      ens
      )
      {
        r.addAll(en.getChildren());
      }
      return r;
    }
    parent : Submission1
    function getParent ( ) : Submission1
    {
      return this.parent;
    }
    static function getParent ( en : Submission2 ) : Submission1
    {
      return if ( en == null ) ( null as Submission1 ) else en.getParent();
    }
    static function getParent ( ens : List<Submission2> ) : List<Submission1>
    {
      return [ en.getParent() | en : Submission2 in ens where en.getParent() != null ];
    }
  }

  entity Submission3 {
    answer : String ( default= null )
    function getAnswer ( ) : String
    {
      return this.answer;
    }
    static function getAnswer ( en : Submission3 ) : String
    {
      return if ( en == null ) ( null as String ) else en.getAnswer();
    }
    static function getAnswer ( entities : List<Submission3> ) : List<String>
    {
      return [ en.getAnswer() | en : Submission3 in entities where en.getAnswer() != null ];
    }
    grade : Float ( default= null )
    function getGrade ( ) : Float
    {
      return this.grade;
    }
    static function getGrade ( en : Submission3 ) : Float
    {
      return if ( en == null ) ( null as Float ) else en.getGrade();
    }
    static function getGrade ( entities : List<Submission3> ) : List<Float>
    {
      return [ en.getGrade() | en : Submission3 in entities where en.getGrade() != null ];
    }
    pass : Bool := calculatePass()
    function getPass ( ) : Bool
    {
      return this.pass;
    }
    static function getPass ( en : Submission3 ) : Bool
    {
      return if ( en == null ) ( null as Bool ) else en.getPass();
    }
    static function getPass ( entities : List<Submission3> ) : List<Bool>
    {
      return [ en.getPass() | en : Submission3 in entities where en.getPass() != null ];
    }
    function calculatePass ( ) : Bool
    {
      return ( Expressions.choice_One_One(Expressions.gte_Float(Submission3.getGrade(this), 5.5), false) as Bool );
    }
    parent : Submission2
    function getParent ( ) : Submission2
    {
      return this.parent;
    }
    static function getParent ( en : Submission3 ) : Submission2
    {
      return if ( en == null ) ( null as Submission2 ) else en.getParent();
    }
    static function getParent ( ens : List<Submission3> ) : List<Submission2>
    {
      return [ en.getParent() | en : Submission3 in ens where en.getParent() != null ];
    }
  }

section  ui

  define

  applicationmenu

  (

  )

  {

  navbaritem
    {
    navigate manageSubmission1() [ ] { "Submission1" }
      }

  navbaritem
    {
    navigate manageSubmission2() [ ] { "Submission2" }
      }

  navbaritem
    {
    navigate manageSubmission3() [ ] { "Submission3" }
      }

  }

  derive

  CRUD

  Submission1

  derive

  CRUD

  Submission2

  derive

  CRUD

  Submission3