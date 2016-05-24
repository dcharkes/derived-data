fn main() {
    let mut lab2Alice = Submission{ children:vec![], answer:"bla".to_string(), manual_grade:9.0 };
    lab2Alice.manual_grade = 8.0;
    println!("Hello, world!");
}

struct Submission {
    children: Vec<Submission>,
    answer: String,
    manual_grade: f32
}