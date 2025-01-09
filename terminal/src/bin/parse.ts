import { Terminal } from "@xterm/xterm";
import { defaultPath } from "../shell";

export async function parse($path: string, path: (_: string) => void, terminal: Terminal, cmd: string, args: string[]) {
  switch (cmd) {
    case "clear":
      terminal.clear();
      break;
    case "ls":
      terminal.writeln(Kotlin.getFiles($path).replace(/\n/g, "\r\n"));
      break;
    case "dir":
      if ($path == "") {
        terminal.write(`\x1b[31mError: No path selected\x1b[m`);
        break;
      }

      terminal.writeln($path);

      break;
    case "cd":
      let newPath = args.join(" ");

      if (newPath.startsWith("~")) {
        newPath = newPath.replace("~", defaultPath);
      }

      if (!Kotlin.validatePath(newPath)) {
        terminal.write(`\x1b[31mError: Invalid Path\x1b[m`);
        break;
      }

      path(newPath)

      break;
    case "cdui":
      terminal.writeln("Contacting UI provider...");

      return new Promise((resolve) => {
        Kotlin.getCwd()
        globalThis.setDir = (val) => {
          path(val)

          terminal.writeln(`UI returned ${val}`);

          resolve(null)
        }
      });
    case "cat":
      terminal.writeln($path)
      break;
    default:
      throw new Error(`Cannot find ${cmd}`)
  }
}