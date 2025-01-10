use std::env::{self, args};

use dlopen2::wrapper::{Container, WrapperApi};

#[derive(WrapperApi)]
struct RunnableSo {
  run: fn(args: Vec<String>) -> (),
}

fn main() {
  let dll = env::var("LOAD_DLL").expect("Unable to find LOAD_DLL environment variable");

  let run: Container<RunnableSo> =
    unsafe { Container::load(dll) }.expect("Unable to load dynalic library");

  let mut args = args().collect::<Vec<String>>();
  args.remove(0);

  println!("Got args: {args:?}");

  // We give this process to the so file so that it can execute as it likes
  run.run(args);
}
