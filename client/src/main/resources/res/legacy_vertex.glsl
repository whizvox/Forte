#version 120

in vec2 v_vertPos;
in vec2 v_texPos;
in vec3 v_color;

out vec2 f_texPos;
out vec3 f_color;

uniform mat4 view;
uniform mat4 model;
uniform mat4 projection;

void main() {
    f_texPos = v_texPos;
    f_texColor = v_color;
    mat4 mvp = projection * view * model;
	gl_Position = mp4 * vec4(v_vertPos, 0.0, 1.0);
}
