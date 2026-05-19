import flet as ft

def main(page: ft.Page):
    page.title = "Seal"
    page.theme_mode = ft.ThemeMode.DARK
    page.bgcolor = "#111210"
    
    page.theme = ft.Theme(
        color_scheme=ft.ColorScheme(
            primary="#98D68A",
            on_primary="#14380E",
            primary_container="#2B5023",
            on_primary_container="#B4F3A4",
            surface="#1A1C18",
            on_surface="#E2E3DC",
            outline="#8C9388"
        )
    )
    
    def navigate(e):
        index = e.control.selected_index
        main_view.visible = (index == 0)
        settings_view.visible = (index == 1)
        downloads_view.visible = (index == 2)
        page.update()

    # الشاشة الرئيسية
    url_input = ft.TextField(
        label="Video link",
        border_color="#8C9388",
        border_radius=12,
        focused_border_color="#98D68A",
        text_style=ft.TextStyle(color="#E2E3DC"),
        label_style=ft.TextStyle(color="#8C9388")
    )
    
    main_view = ft.Container(
        content=ft.Column([
            ft.Row([ft.Text("Seal", size=42, weight=ft.FontWeight.W_400, color="#E2E3DC")]),
            ft.VerticalDivider(height=40, color=ft.colors.TRANSPARENT),
            url_input,
        ]),
        expand=True,
        visible=True,
        padding=20
    )

    # واجهة الإعدادات
    settings_view = ft.Container(
        content=ft.Column([
            ft.Text("Settings", size=32, color="#E2E3DC"),
            ft.VerticalDivider(height=20, color=ft.colors.TRANSPARENT),
            ft.ListTile(leading=ft.Icon(ft.icons.SETTINGS_OUTLINED, color="#98D68A"), title=ft.Text("General"), subtitle=ft.Text("Yt-dlp version, notification, playlist")),
            ft.ListTile(leading=ft.Icon(ft.icons.FOLDER_OUTLINED, color="#98D68A"), title=ft.Text("Download directory"), subtitle=ft.Text("Select where to store files")),
            ft.ListTile(leading=ft.Icon(ft.icons.AUDIO_FILE_OUTLINED, color="#98D68A"), title=ft.Text("Format"), subtitle=ft.Text("File format, video quality, subtitles")),
        ], scroll=ft.ScrollMode.AUTO),
        expand=True,
        visible=False,
        padding=20
    )

    # واجهة التنزيلات
    downloads_view = ft.Container(
        content=ft.Column([
            ft.Text("Downloads", size=32, color="#E2E3DC"),
            ft.Row([ft.ChoiceChip(label=ft.Text("Audio"), selected=True), ft.ChoiceChip(label=ft.Text("Video"), selected=False)]),
            ft.VerticalDivider(height=20, color=ft.colors.TRANSPARENT),
            ft.Row([
                ft.Image(src="assets/seal_icon.png", width=80, height=80, fit=ft.ImageFit.COVER, radius=10),
                ft.Column([
                    ft.Text("SBR JoJo Part 8 - Preview", weight=ft.FontWeight.BOLD, size=16),
                    ft.Text("Warner Bros. Japan Anime", size=12, color="#8C9388"),
                    ft.Text("12.44 MB", size=12, color="#98D68A")
                ])
            ])
        ]),
        expand=True,
        visible=False,
        padding=20
    )

    def show_download_opts(e):
        bs.open = True
        page.update()

    def close_bs(e):
        bs.open = False
        page.update()

    bs = ft.BottomSheet(
        container=ft.Container(
            content=ft.Column([
                ft.Row([ft.Icon(ft.icons.DONE_ALL), ft.Text("Configure before download", size=18, weight=ft.FontWeight.BOLD)], alignment=ft.MainAxisAlignment.CENTER),
                ft.Text("Download type", color="#98D68A", weight=ft.FontWeight.BOLD),
                ft.Row([
                    ft.ElevatedButton("✓ Audio", style=ft.ButtonStyle(bgcolor="#2B5023", color="#B4F3A4")),
                    ft.OutlinedButton("Video")
                ]),
                ft.VerticalDivider(height=20),
                ft.Row([
                    ft.TextButton("X Cancel", on_click=close_bs, style=ft.ButtonStyle(color=ft.colors.RED_ACCENT)),
                    ft.ElevatedButton("✓ Download", style=ft.ButtonStyle(bgcolor="#98D68A", color="#14380E"))
                ], alignment=ft.MainAxisAlignment.END)
            ], tight=True, padding=20),
            bgcolor="#1A1C18",
            border_radius=ft.border_radius.only(top_left=24, top_right=24)
        )
    )
    page.overlay.append(bs)

    fab_buttons = ft.Positioned(
        bottom=20,
        right=20,
        content=ft.Column([
            ft.FloatingActionButton(icon=ft.icons.ASSIGNMENT_OUTLINED, bgcolor="#2B5023", icon_color="#B4F3A4", mini=True),
            ft.FloatingActionButton(icon=ft.icons.DOWNLOAD, bgcolor="#98D68A", icon_color="#14380E", on_click=show_download_opts),
        ], spacing=10)
    )

    nav_bar = ft.NavigationBar(
        destinations=[
            ft.NavigationDestination(icon=ft.icons.HOME_OUTLINED, selected_icon=ft.icons.HOME, label="Home"),
            ft.NavigationDestination(icon=ft.icons.SETTINGS_OUTLINED, selected_icon=ft.icons.SETTINGS, label="Settings"),
            ft.NavigationDestination(icon=ft.icons.DOWNLOAD_DONE, label="Library")
        ],
        on_change=navigate,
        bgcolor="#1A1C18"
    )

    page.add(
        ft.Stack([
            ft.Column([main_view, settings_view, downloads_view], expand=True),
            fab_buttons
        ], expand=True),
        nav_bar
    )

ft.app(target=main)
  
